package com.danielkim.soundrecorder.edit;

import com.danielkim.soundrecorder.edit.exceptions.NotImplementedException;

public class MergedChunk extends AudioChunk {
    public MergedChunk(AudioChunk pre, AudioChunk post, boolean removeSilence) {
        this.pre = pre;
        this.post = post;

        this.setStartIndex(pre.startIndex);
        if(removeSilence){
            post.setStartIndex(pre.getEndIndex());
        }else{
            post.setStartIndex(post.getStartIndex() - pre.getStartIndex());
        }
        pre.setStartIndex(0);
    }

    private AudioChunk pre, post;
    private long getSilence(){
        return post.getStartIndex() - pre.getEndIndex();
    }
    @Override
    public long getLength() {
        return post.getEndIndex() - pre.getStartIndex();
    }

    @Override
    public float getSample(long sampleIndex) {
        throw new NotImplementedException();
    }

    @Override
    public long getSamples(long startSampleIndex, float[] returnedSamples) {
        if(startSampleIndex < pre.getEndIndex()){
            return pre.getSamples(startSampleIndex, returnedSamples);
        }else if(startSampleIndex < post.getStartIndex()){
            int i;
            for(i = 0; i < returnedSamples.length && i < getSilence(); i++){
                returnedSamples[i] = 0;
            }
            return i;
        }else{
            return  post.getSamples(startSampleIndex - post.getStartIndex(), returnedSamples);
        }
    }
}
