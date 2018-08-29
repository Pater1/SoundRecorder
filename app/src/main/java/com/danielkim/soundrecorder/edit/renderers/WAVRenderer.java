package com.danielkim.soundrecorder.edit.renderers;

import com.danielkim.soundrecorder.edit.AudioProvider;
import com.danielkim.soundrecorder.edit.helpers.FileHelper;
import com.danielkim.soundrecorder.edit.helpers.PCMHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.io.*;
import java.nio.channels.FileChannel;

import be.tarsos.dsp.writer.WaveHeader;

public class WAVRenderer implements Renderer {
    private Long length;
    public WAVRenderer(){
        length = null;
    };
    public WAVRenderer(long preAllocatedLength){
        length = preAllocatedLength;
    }

    @Override
    public String extention(){
        return ".wav";
    }
    @Override
    public String render(String fileName, String folderPath, AudioProvider audio) throws IOException {
        return render(fileName, folderPath, audio, 0, audio.getLength());
    }
    @Override
    public String render(String fileName, String folderPath, AudioProvider audio, long start, long end) throws IOException {
        String file = FileHelper.setupFile(fileName, folderPath, extention());

        if(length == null){
            length = audio.getLength();
        }

        WaveHeader h = new WaveHeader();
        ByteArrayOutputStream s = new ByteArrayOutputStream(44);
        h.setBitsPerSample((short)16);
        h.setNumChannels((short)1);
        h.setSampleRate((int)audio.getSampleRate());
        h.setFormat(WaveHeader.FORMAT_PCM);
        h.setNumBytes((int)(long)length * 2);

        FileOutputStream stream = new FileOutputStream(file);
        h.write(stream);

        float[] audioBuffer = new float[512];
        long length = start, tmp = 0;
        do{
            tmp = audio.getSamples(length, audioBuffer);
            short[] asSamples = PCMHelper.convert32bitSamplesPCMto16bitPCM(audioBuffer);
            ByteBuffer asBytes = ByteBuffer.allocate((int)asSamples.length * 2);
            asBytes.order(ByteOrder.LITTLE_ENDIAN);
            for(int i = 0; i < tmp; i++){
                asBytes.putShort(asSamples[i]);
            }
            stream.write(asBytes.array());
            length += tmp >= 0? tmp: 0;
        }while (tmp >= 0 && length < end);

        stream.close();

        return file;
    }
}
