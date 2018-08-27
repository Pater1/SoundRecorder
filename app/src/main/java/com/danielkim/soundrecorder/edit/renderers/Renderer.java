package com.danielkim.soundrecorder.edit.renderers;

import com.danielkim.soundrecorder.edit.AudioProvider;

import java.io.FileNotFoundException;
import java.io.IOException;

import it.sauronsoftware.jave.EncoderException;

public interface Renderer {
    public String render(String file, String path, AudioProvider audio) throws IOException, EncoderException;
    public String extention();
}
