package com.danielkim.soundrecorder.edit;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.danielkim.soundrecorder.edit.helpers.TimeHelper;

public class AudioPlayer {
    private static final int BUFFER_SIZE = 512;
    private AudioProvider provider;
    private AudioTrack audioTrack;
    private boolean shouldContinue, paused;
    private AudioPlayerOnFinishedListener listener;
    private Thread feedThread;
    private long playbackHead;
    private int bufferSize;
    private Runnable runPlayback;
    public AudioPlayer(AudioProvider audioProvider){
        provider = audioProvider;
        shouldContinue = true;
        paused = false;
        playbackHead = 0;
        listener = null;
        bufferSize = AudioTrack.getMinBufferSize((int)provider.getSampleRate()
                , AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_FLOAT);

        runPlayback = new Runnable() {
            @Override
            public void run() {
                float[] audioBuffer = new float[bufferSize];
                long tmp = 0;
                do{
                    while (paused){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    tmp = provider.getSamples(playbackHead, audioBuffer);

                    int r = audioTrack.write(audioBuffer, 0, (int)tmp, AudioTrack.WRITE_BLOCKING);

                    playbackHead += r > tmp? tmp: r;
                }while (tmp >= 0 && shouldContinue);

                if(listener != null){
                    listener.onCompletion();
                }
            }
        };

        buildTrack();
    }

    private void buildTrack(){
        if(audioTrack != null){
            audioTrack.release();
        }
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                (int)provider.getSampleRate(),
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_FLOAT,
                bufferSize,
                AudioTrack.MODE_STREAM);

        feedThread = new Thread(runPlayback);
        feedThread.setPriority(Thread.MAX_PRIORITY);
        feedThread.start();
        play();
    }

    public void play(){
        try {
            paused = false;
            audioTrack.play();
        }catch (IllegalStateException e){
            buildTrack();
            play();
        }
    }
    public void pause(){
        try {
            audioTrack.pause();
            paused = true;
        }catch (IllegalStateException e){
            buildTrack();
            pause();
        }
    }
    public void stop(){
        try{
            setPlaybackHead(0);
            audioTrack.pause();
            paused = true;
        }catch (IllegalStateException e){
            buildTrack();
            stop();
        }
    }

    public int getSampleRate(){
        return (int)provider.getSampleRate();
    }

    public int getDuration(){
        return (int)TimeHelper.microsecondToMillisecond( TimeHelper.sampleIndexToMicrosecond( provider.getLength(), (int)provider.getSampleRate()));
    }

    public long getPlaybackHead() {
        return playbackHead;
    }
    public void setPlaybackHead(long playbackHead) {
        try{
            paused = true;
            audioTrack.pause();
            audioTrack.flush();
            this.playbackHead = playbackHead;
            paused = false;
            audioTrack.play();
        }catch (IllegalStateException e){
            buildTrack();
            setPlaybackHead(playbackHead);
        }
    }

    public void release(){
        try{
            stop();
        }catch (IllegalStateException e){}
        paused = false;
        shouldContinue = false;
        audioTrack.release();
    }

    public interface AudioPlayerOnFinishedListener{
        void onCompletion();
    }
    public AudioPlayerOnFinishedListener getListener() {
        return listener;
    }
    public void setListener(AudioPlayerOnFinishedListener listener) {
        this.listener = listener;
    }
}
