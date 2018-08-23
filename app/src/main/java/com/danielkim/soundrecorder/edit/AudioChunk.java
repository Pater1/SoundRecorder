package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.EffectTarget;
import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.EventHandler;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public abstract class AudioChunk implements AudioProvider, EventHandler {
	protected long startIndex;

	public abstract long getLength();
	public long getEndIndex(){
		return getStartIndex() + getLength();
	}
	public long getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(long index) {
		if (index >= 0) {
			startIndex = index;
		}
	}

	protected long sampleRate;
	@Override
	public long getSampleRate() {
		return sampleRate;
	}
	@Override
	public void setSampleRate(long setTo) {
		sampleRate = setTo;
	}

	@Override
	public boolean handleEvent(Event toHandle) {
		return toHandle.applyEvent(this);
	}

	@Override
	public int getTargetedFlag(){
		return EffectTarget.CHUNK;
	}

}
