package com.danielkim.soundrecorder.edit.renderers;

import com.danielkim.soundrecorder.edit.AudioProvider;

public interface Renderer {
    public void render(String file, AudioProvider audio);
}
