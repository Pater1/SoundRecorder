package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import be.tarsos.dsp.writer.WaveHeader;

public class WAVAudioChunk extends AudioChunk {
    private static final int signed24bMax = 255 | 255<<8 | 127<<16;
    private static final int signed24bMin = 128<<16;
    private static final int unsigned24bMax = 255 | 255<<8 | 255<<16;

    private WaveHeader header;
    private File readFrom;
    FileInputStream fileInputStream;
    private int intMask = 0;
    private long lastSampleEnd;
    public WAVAudioChunk(File file) throws IOException {
        readFrom = file;
        this.setSampleRate(sampleRate);

        this.fileInputStream = new FileInputStream(file);
        byte[] b = new byte[44];
        this.fileInputStream.read(b);
        this.fileInputStream.close();

        header = new WaveHeader();
        header.setNumChannels((short)((b[22]&0xff) | (b[23]&0xff)<<8));
        header.setSampleRate((b[24]&0xff) | (b[25]&0xff)<<8 | (b[26]&0xff)<<16 | (b[27]&0xff)<<24);
        setSampleRate(header.getSampleRate());
        header.setBitsPerSample((short)((b[34]&0xff) | (b[35]&0xff)<<8));
        header.setNumBytes((b[0x28]&0xff) | (b[0x29]&0xff)<<8 | (b[0x2A]&0xff)<<16 | (b[0x2B]&0xff)<<24);
        header.setFormat((short)((b[20]&0xff) | (b[21]&0xff)<<8));
        intMask = 0;
        for(int i = 0; i < header.getBitsPerSample(); i++){
            intMask |= 1<<i;
        }
        lastSampleEnd = Long.MAX_VALUE;

        if(header.getFormat() != 1){
            throw new RuntimeException("Playback of non-PCM WAV files is not supported!");
        }
    }
    @Override
    public float getSample(long sampleIndex) {
        throw new RuntimeException("single sample access not supported!");
    }


    private int getMax(int bitsPerSample) {
        switch (bitsPerSample){
            case 24:
                return signed24bMax;
            case 16:
                return Short.MAX_VALUE;
            case 8:
                return Byte.MAX_VALUE;
            default:
                int ret = 0;
                for(int i = 0; i <= bitsPerSample; i++){
                    ret |= 1<<i;
                }
                return ret;
        }
    }
    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        try {
            if (startSampleIndex < lastSampleEnd) {
                fileInputStream.close();
                fileInputStream = new FileInputStream(readFrom);
                fileInputStream.skip(44);
                lastSampleEnd = 0;
            }
            if(startSampleIndex > lastSampleEnd){
                fileInputStream.skip(startSampleIndex);
                lastSampleEnd = startSampleIndex;
            }

            byte[] raw = new byte[returnedSamples.length * (header.getBitsPerSample()/8)];
            long ret = fileInputStream.read(raw) / (header.getBitsPerSample()/8);

            int pntr = 0;
            for(int i = 0; i < ret; i++){
                int r = 0;
                for(int j = 0; j < (header.getBitsPerSample()/8); j++){
                    r |= (raw[pntr++] & 0xFF) << (j*8);
                }
                returnedSamples[i] = (float)r / getMax(header.getBitsPerSample());
                if(returnedSamples[i] > 1){
                    returnedSamples[i] -= 2;
                }
            }

            if(ret == 0){
                return -1;
            }
            return ret;
        }catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return -1;
    }

    @Override
    public long getLength() {
        return header.getNumBytes() / (header.getBitsPerSample() / 8);
    }
}
