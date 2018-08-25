package com.danielkim.soundrecorder.edit.renderers;

import com.danielkim.soundrecorder.edit.AudioProvider;
import com.danielkim.soundrecorder.edit.helpers.PCMHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.io.*;

import be.tarsos.dsp.writer.WaveHeader;

public class WAVRenderer implements Renderer {
    @Override
    public void render(String filePath, AudioProvider audio) {
        WaveHeader h = new WaveHeader();
        h.setBitsPerSample((short)16);
        h.setNumChannels((short)1);
        h.setSampleRate((int)audio.getSampleRate());
        h.setFormat(WaveHeader.FORMAT_PCM);
        h.setNumBytes((int)audio.getLength() * 2);

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(filePath);
            try {
                try {
                    //stream.write(headerBuffer.array());
                    h.write(stream);
                    float[] audioBuffer = new float[512];
                    long length = 0, tmp = 0;
                    do{
                        tmp = audio.getSamples(length, audioBuffer);
                        short[] asSamples = PCMHelper.convert32bitSamplesPCMto16bitPCM(audioBuffer);
                        ByteBuffer asBytes = ByteBuffer.allocate((int)asSamples.length * 2);
                        asBytes.order(ByteOrder.LITTLE_ENDIAN);
                        for(int i = 0; i < tmp; i++){
                            asBytes.putShort(asSamples[i]);
                        }
                        stream.write(asBytes.array());
                        length += tmp;
                    }while (tmp > 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
