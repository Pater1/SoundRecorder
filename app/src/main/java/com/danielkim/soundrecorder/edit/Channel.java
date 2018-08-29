package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.events.EffectTarget;
import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.EventHandler;
import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Channel implements AudioProvider, EventHandler {
	private List<AudioChunk> data = new ArrayList<>();
	private ChannelHeader header = new ChannelHeader();

	public int chunkCount(){
		return data.size();
	}
	
	public void add(AudioChunk chunk) {
		data.add(chunk);
		data.sort(new Comparator<AudioChunk>() {
			@Override
			public int compare(AudioChunk o1, AudioChunk o2) {
				return (int)(o1.getStartIndex() - o2.getStartIndex());
			}
		});
	}

    public void remove(AudioChunk chunk) {
        data.remove(chunk);
    }
    public void remove(int index) {
        data.remove(index);
    }
    public void remove(long sample) {
        data.remove(getChunkForIndex(sample));
    }

	public AudioChunk get(int index) {
		return data.get(index);
	}
	public AudioChunk get(long sample) { return getChunkForIndex(sample); }
	
	public boolean contains(AudioChunk chunk) {
		return data.contains(chunk);
	}

	@Override
	public long getLength() {
		long lastEnd = 0;
		for(AudioChunk c: data){
			long l = c.getEndIndex();
			if(l > lastEnd){
				lastEnd = l;
			}
		}
		return lastEnd;
	}

	@Override
	public float getSample(long sampleIndex) {
		for(AudioChunk c: data){
			float l = c.getSample(sampleIndex - c.getStartIndex());
			if(Math.abs(l) < 1E-5){
				return l;
			}
		}
		return 0;
	}

	@Override
	public long getSamples(long startSampleIndex, float[] returnedSamples) {
		boolean startWithinRange = false;
		for(AudioChunk c: data){
			long l = c.getSamples(startSampleIndex - c.getStartIndex(), returnedSamples);
			if(l > 0){
				return l;
			}

			if(startSampleIndex < c.getEndIndex()){
				startWithinRange = true;
			}
		}

		if(startWithinRange) {
			//buffer is within renderable section of channel, but falls entirely within a gap between chunks
			for(int i = 0; i < returnedSamples.length; i++){
				returnedSamples[i] = 0.0f;
			}
			return returnedSamples.length;
		}else{
			return -1;
		}
	}
	
	public int getDataSize() {
		return data.size();
	}

	private long sampleRate;
	@Override
	public long getSampleRate() {
		return sampleRate;
	}
	@Override
	public void setSampleRate(long setTo) {
		sampleRate = setTo;
	}

	public AudioChunk getChunkForIndex(long sampleIndex){
		for(AudioChunk c: data){
			if(sampleIndex >= c.getStartIndex() && sampleIndex < c.getEndIndex()){
				return c;
			}
		}
		return null;
	}
	public List<AudioChunk> getChunksForIndexes(long sampleIndexStart, long sampleIndexEnd){
		List<AudioChunk> ret = new ArrayList<>();
		for(AudioChunk c: data){
			if(!(c.getEndIndex() < sampleIndexStart || c.getStartIndex() > sampleIndexEnd)){
				ret.add(c);
			}
		}
		Collections.sort(ret, new Comparator<AudioChunk>() {
			@Override
			public int compare(AudioChunk t0, AudioChunk t1) {
				return (int)(t0.getStartIndex() - t1.getStartIndex());
			}
		});
		return ret;
	}
	public List<AudioChunk> getFlankingChunks(long sampleIndex){
		List<AudioChunk> ret = new ArrayList<>();
		ret.add(null);
		ret.add(null);
		for(AudioChunk c: data){
			if(c.getEndIndex() < sampleIndex && (ret.get(0) == null || c.getEndIndex() > ret.get(0).getEndIndex())){
				ret.set(0, c);
			}

			if(c.getStartIndex() > sampleIndex && (ret.get(1) == null || c.getStartIndex() < ret.get(1).getStartIndex())){
				ret.set(1, c);
			}
		}
		while (ret.contains(null)){
			ret.remove(null);
		}
		return ret;
	}
	@Override
	public int getTargetedFlag(){
		return EffectTarget.CHANNEL;
	}
	@Override
	public boolean handleEvent(Event toHandle) {
		if(!toHandle.applyEvent(this)){
			AudioChunk eA = getChunkForIndex(toHandle.getEffectStartSampleIndex());
			AudioChunk eB = getChunkForIndex(toHandle.getEffectStartSampleIndex());
			boolean bA = false, bB = false;
			if(eA != null){
				bA = eA.handleEvent(toHandle);
			}
			if(eB != null && eB != eA){
				bB = eB.handleEvent(toHandle);
			}
			return bA || bB;
		}
		return true;
	}
}
