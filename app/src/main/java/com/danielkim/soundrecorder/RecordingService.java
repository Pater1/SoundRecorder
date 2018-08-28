package com.danielkim.soundrecorder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.danielkim.soundrecorder.activities.MainActivity;
import com.danielkim.soundrecorder.edit.AudioProvider;
import com.danielkim.soundrecorder.edit.BufferedAudioProvider;
import com.danielkim.soundrecorder.edit.helpers.TimeHelper;
import com.danielkim.soundrecorder.edit.renderers.WAVRenderer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Daniel on 12/28/2014.
 */
public class RecordingService extends Service {
    private static final int SAMPLE_RATE = 44100;
    private static final String LOG_TAG = "RecordingService";

    public String getmFileName() {
        return mFileName;
    }
    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getmFilePath() {
        return mFilePath;
    }
    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public long getmStartingTimeMillis() {
        return mStartingTimeMillis;
    }
    public void setmStartingTimeMillis(long mStartingTimeMillis) {
        this.mStartingTimeMillis = mStartingTimeMillis;
    }

    public long getmElapsedMillis() {
        return mElapsedMillis;
    }
    public void setmElapsedMillis(long mElapsedMillis) {
        this.mElapsedMillis = mElapsedMillis;
    }

    public int getmElapsedSeconds() {
        return mElapsedSeconds;
    }
    public void setmElapsedSeconds(int mElapsedSeconds) {
        this.mElapsedSeconds = mElapsedSeconds;
    }

    private String mFileName = null;
    private String mFilePath = null;

    private MediaRecorder mRecorder = null;

    private DBHelper mDatabase;

    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private int mElapsedSeconds = 0;
    private OnTimerChangedListener onTimerChangedListener = null;
    private static final SimpleDateFormat mTimerFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    private Timer mTimer = null;
    private TimerTask mIncrementTimerTask = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface OnTimerChangedListener {
        void onTimerChanged(int seconds);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = new DBHelper(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //if (mRecorder != null) {
            stopRecording();
        //}

        super.onDestroy();
    }

    private AudioRecord record;
    private boolean mShouldContinue;
    private static short[] buffer = new short[44100 * 60 * 30];
    private int length = 0;
    public void startRecording() {
        setFileNameAndPath();

        record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    SAMPLE_RATE * 2);
        record.startRecording();
        mShouldContinue = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                length = 0;
                while (mShouldContinue){
                    length += record.read(buffer, length, buffer.length-length);
                }
            }
        }).start();

        //startTimer();

        /*mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        if (MySharedPreferences.getPrefHighQuality(this)) {
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setAudioEncodingBitRate(192000);
        }

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();

            //startTimer();
            //startForeground(1, createNotification());

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }*/
    }

    public void setFileNameAndPath(){
        int count = 0;
        File f;

        do{
            count++;

            mFileName = getString(R.string.default_file_name)
                    + "_" + (mDatabase.getCount() + count) + "";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/SoundRecorder";

            f = new File(mFilePath);
        }while (f.exists() && !f.isDirectory());
    }

    public void stopRecording() {
        //mRecorder.stop();
        //mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        //mRecorder.release();
        record.stop();
        mShouldContinue = false;
        //Toast.makeText(this, getString(R.string.toast_recording_finish) + " " + mFilePath, Toast.LENGTH_LONG).show();

        //remove notification
        /*if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }*/

        //mRecorder = null;
        try {
            final AudioProvider prov = new BufferedAudioProvider(buffer, 44100, length);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new WAVRenderer().render(mFileName, mFilePath, prov);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RecordingService.this, "File " + mFilePath+File.separator+mFileName+".wav Saved!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            mDatabase.addRecording(mFileName+".wav", mFilePath+File.separator+mFileName+".wav",
                    TimeHelper.microsecondToMillisecond(
                        TimeHelper.sampleIndexToMicrosecond(prov.getLength(), (int)prov.getSampleRate())
                    )
            );

        } catch (Exception e){
            Log.e(LOG_TAG, "exception", e);
        }
        record.release();
    }

    private void startTimer() {
        mTimer = new Timer();
        mIncrementTimerTask = new TimerTask() {
            @Override
            public void run() {
                mElapsedSeconds++;
                if (onTimerChangedListener != null)
                    onTimerChangedListener.onTimerChanged(mElapsedSeconds);
                NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mgr.notify(1, createNotification());
            }
        };
        mTimer.scheduleAtFixedRate(mIncrementTimerTask, 1000, 1000);
    }

    //TODO:
    private Notification createNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_mic_white_36dp)
                        .setContentTitle(getString(R.string.notification_recording))
                        .setContentText(mTimerFormat.format(mElapsedSeconds * 1000))
                        .setOngoing(true);

        mBuilder.setContentIntent(PendingIntent.getActivities(getApplicationContext(), 0,
                new Intent[]{new Intent(getApplicationContext(), MainActivity.class)}, 0));

        return mBuilder.build();
    }
}
