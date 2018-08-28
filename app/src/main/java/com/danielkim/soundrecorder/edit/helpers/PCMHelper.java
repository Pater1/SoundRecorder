package com.danielkim.soundrecorder.edit.helpers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PCMHelper {
    private static final ByteOrder defaultByteOrder = ByteOrder.LITTLE_ENDIAN;
    public static byte[] convert16bitPCMTo8bitWAV(short[] data) {
        ByteBuffer bb = ByteBuffer.allocate(data.length * 2);
        bb.order(defaultByteOrder);
        for (int i = 0; i < data.length; i++) {
            bb.putShort(data[i]);
        }
        return bb.array();
    }
    public static short[] convert8bitWAVTo16bitPCM(byte[] data) {
        ByteBuffer bb = ByteBuffer.allocate(data.length);
        bb.order(defaultByteOrder);
        short[] resultData = new short[data.length / 2];
        for (int i = 0; i < data.length; i+=2) {
            bb.put(data[i]);
            bb.put(data[i+1]);

            resultData[i/2] = bb.getShort(i);
        }
        return resultData;
    }

    public static float[] convert16bitPCMto32bitSamples(short[] data){
        float[] resultData = new float[data.length];
        for(int i = 0; i < data.length; i++){
            resultData[i] = (float)data[i] / Short.MAX_VALUE;
        }
        return resultData;
    }
    public static short[] convert32bitSamplesPCMto16bitPCM(float[] data){
        short[] resultData = new short[data.length];
        for(int i = 0; i < data.length; i++){
            resultData[i] = (short)(data[i] * Short.MAX_VALUE);
        }
        return resultData;
    }
}
