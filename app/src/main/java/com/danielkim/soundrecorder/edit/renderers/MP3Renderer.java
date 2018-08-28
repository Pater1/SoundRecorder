package com.danielkim.soundrecorder.edit.renderers;

import android.os.Environment;

import com.danielkim.soundrecorder.edit.AudioProvider;
import com.danielkim.soundrecorder.edit.helpers.FileHelper;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

public class MP3Renderer implements Renderer {
    @Override
    public String render(String fileName, String folderPath, AudioProvider audio) throws IOException, EncoderException {
        String wav = new WAVRenderer().render(fileName, folderPath, audio);
        File source = new File(wav);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String file = FileHelper.setupFile(fileName, folderPath, extention());
        File target = new File(file);
        AudioAttributes audioAttr = new AudioAttributes();
        audioAttr.setCodec("libmp3lame");
        audioAttr.setBitRate(new Integer(128000/2));
        audioAttr.setChannels(new Integer(1));
        audioAttr.setSamplingRate(new Integer(44100));
        EncodingAttributes encodeAttr = new EncodingAttributes();
        encodeAttr.setFormat("mp3");
        encodeAttr.setAudioAttributes(audioAttr);
        Encoder encoder = new Encoder();
        //try {
            encoder.encode(source, target, encodeAttr);
        /*} catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InputFormatException e) {
            e.printStackTrace();
        } catch (EncoderException e) {
            e.printStackTrace();
        }*/

        source.delete();

        return file;
    }

    @Override
    public String extention() {
        return ".mp3";
    }
}
