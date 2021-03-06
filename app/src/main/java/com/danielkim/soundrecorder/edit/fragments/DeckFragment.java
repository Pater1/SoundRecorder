package com.danielkim.soundrecorder.edit.fragments;

import android.app.Activity;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.Deck;
import com.danielkim.soundrecorder.edit.canvases.AudioChunkCanvas;
import com.danielkim.soundrecorder.edit.canvases.ChannelCanvas;
import com.danielkim.soundrecorder.edit.canvases.DeckCursorCanvas;
import com.danielkim.soundrecorder.edit.editingoptions.Option;
import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.events.MoveEvent;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeckFragment extends Fragment {
	
	public static final int MARGIN = 10;
	public static final int CHANNEL_HEIGHT = 300;
	
	public static DeckFragment instance;
	private Deck deck;
	private LinearLayout channelLinearLayout;
	private int cursorChannelIndex;
	private int greatestChannelLength;
	private AudioChunk chunkDragged;
	private boolean isDragging;
	private float horizontalDisplacement;
	
	public Deck getDeck(){
		return deck;
	}
	
	public DeckFragment() {
		// Required empty public constructor
		Option.setUpdateFragment(this);
		instance = this;
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment DeckFragment.
	 */
	public static DeckFragment newInstance(Deck deck) {
		DeckFragment fragment = new DeckFragment();
		Bundle args = new Bundle();
		fragment.deck = deck;
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_deck, container, false);
		channelLinearLayout = (LinearLayout) v.findViewById(R.id.channelLinearLayout);
		updateDeckView();
		v.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		return v;
	}
	
	public void updateDeckView() {
		if (deck != null) {
			channelLinearLayout.removeAllViews();
			
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			
			int sampleBegin = (int) (horizontalDisplacement / AudioChunkCanvas.GAP);
			int sampleEnd = sampleBegin + (size.x / AudioChunkCanvas.GAP);
			
			if (sampleEnd == 0) {
				Log.d("end_deckfrag", sampleEnd + "");
				Log.d("begin_deckfrag", sampleBegin + "");
			}
			
			for(int curIndex = 0; curIndex < deck.getChannelCount(); curIndex++){
				Channel curChannel = deck.getChannel(curIndex);
				ChannelCanvas channelCanvas = new ChannelCanvas(getActivity(), curChannel, curIndex, this);
				
				channelCanvas.setSampleBegin(sampleBegin);
				channelCanvas.setSampleEnd(sampleEnd);
				
				channelLinearLayout.addView(channelCanvas);
				
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) channelCanvas.getLayoutParams();
				params.bottomMargin = MARGIN;
				params.topMargin = MARGIN;
				params.leftMargin = MARGIN;
				params.rightMargin = MARGIN;
				channelCanvas.setLayoutParams(params);
			}
			
			this.invalidate();
		}
	}
	
	
	public void refresh() {
		updateDeckView();
		resizeMax();
	}
	
	public void invalidate() {
		for (int i = 0; i < channelLinearLayout.getChildCount(); i++) {
			ChannelCanvas channelCanvas = (ChannelCanvas) channelLinearLayout.getChildAt(i);
			channelCanvas.invalidate();
		}
	}
	
	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);
	}
	
	public void updateCursor(int selectedChannelIndex) {
		if (selectedChannelIndex != cursorChannelIndex) {
			ChannelCanvas prevCursorChannel = ((ChannelCanvas) channelLinearLayout.getChildAt(cursorChannelIndex));
			if (prevCursorChannel != null) {
				prevCursorChannel.disableCursor();
				prevCursorChannel.invalidate();
			}
			cursorChannelIndex = selectedChannelIndex;
		}
	}
	
	public long[] getCursorPoints() {
		if (channelLinearLayout.getChildCount() == 0 || cursorChannelIndex >= channelLinearLayout.getChildCount()) {
			return null;
		}
		
		ChannelCanvas channelCanvas = (ChannelCanvas) channelLinearLayout.getChildAt(cursorChannelIndex);
		return channelCanvas.getCursor();
	}
	
	public int getCursorChannelIndex() {
		return cursorChannelIndex;
	}
	
	public void resizeMax() {
		int maxWidth = 0;
		for (int i = 0; i < channelLinearLayout.getChildCount(); i++) {
			ChannelCanvas canvas = (ChannelCanvas) channelLinearLayout.getChildAt(i);
			canvas.resizeHeight(CHANNEL_HEIGHT);
			maxWidth = canvas.getLayoutParams().width;
		}
		
		resize(maxWidth);
	}
	
	public void resize(int width) {
		int height = 0;
		
		for (int i = 0; i < channelLinearLayout.getChildCount(); i++) {
			ChannelCanvas canvas = (ChannelCanvas) channelLinearLayout.getChildAt(i);
			height += CHANNEL_HEIGHT + (2 * MARGIN);
			int layoutWidth = canvas.getLayoutParams().width;
			if (layoutWidth < width) {
				canvas.resize(width, CHANNEL_HEIGHT);
			}
			greatestChannelLength = Math.max(canvas.getLayoutParams().width, greatestChannelLength);
		}
		
		DeckCursorCanvas deckCursorCanvas = (DeckCursorCanvas) getActivity().findViewById(R.id.deckCursorCanvas);
		deckCursorCanvas.resize(getGreatestChannelLength(), height);
	}
	
	public int getGreatestChannelLength() {
		return greatestChannelLength;
	}
	
	public void setDeck(Deck d) {
		deck = d;
		updateDeckView();
	}
	
	public void renderAudio(String fileName) {
		try {
			String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoundRecorder";
			String file = deck.render(dir, fileName);
			Toast.makeText(getActivity(), "File saved to " + file, Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void trySwitchChannel(MotionEvent e, AudioChunk chunk, ChannelCanvas curHostChannelCanvas) {
		ChannelCanvas nextHost = null;
		
		if (e.getY() < 0 && curHostChannelCanvas.getChannelIndex() > 0) {
			nextHost = (ChannelCanvas) channelLinearLayout.getChildAt(curHostChannelCanvas.getChannelIndex() - 1);
		} else if (e.getY() > 0 && curHostChannelCanvas.getChannelIndex() < channelLinearLayout.getChildCount()) {
			nextHost = (ChannelCanvas) channelLinearLayout.getChildAt(curHostChannelCanvas.getChannelIndex() + 1);
		}
		
		if (nextHost != null) {
			if (!nextHost.contains(chunk)) {
				Event moveEvent = new MoveEvent(chunk.getStartIndex(), chunk.getEndIndex(),
						curHostChannelCanvas.getChannelIndex(), nextHost.getChannelIndex());
				if (moveEvent.handleEvent()) {
//					curHostChannelCanvas.removeChunk(chunk);
//					curHostChannelCanvas.resize(curHostChannelCanvas.getWidth(), CHANNEL_HEIGHT);
//					nextHost.resize(nextHost.getWidth(), CHANNEL_HEIGHT);
					
					long downTime = SystemClock.uptimeMillis();
					curHostChannelCanvas.onTouchEvent(
							MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP, 0, 0, 0));
					
					long nextDownTime = SystemClock.uptimeMillis() + 10;
					nextHost.onTouchEvent(MotionEvent.obtain(nextDownTime, nextDownTime,
							MotionEvent.ACTION_DOWN, e.getX(), e.getY(), 0));
				}
			}
			
			curHostChannelCanvas.invalidate();
			nextHost.invalidate();
		}
	}
	
	public void refreshChannel(int channel) {
		if (channelLinearLayout.getChildCount() > channel) {
			ChannelCanvas channelCanvas = (ChannelCanvas) channelLinearLayout.getChildAt(channel);
			channelCanvas.regenChunks();
		}
	}
	
	public boolean isDragging() {
		return isDragging;
	}
	
	public void startDragging(AudioChunk chunkDragged) {
		this.chunkDragged = chunkDragged;
		setIsDragging(true);
	}
	
	public void stopDragging() {
		this.chunkDragged = null;
		setIsDragging(false);
	}
	
	public void scrollHorizontally(int magnitude) {
		horizontalDisplacement += magnitude;
		if (horizontalDisplacement < 0) {
			horizontalDisplacement = 0;
		}
		HorizontalScrollView horizontalScrollView = getHorizontalScrollView();
		horizontalScrollView.scrollTo((int) horizontalDisplacement, 0);
		updateScroll();
//		Log.d("scroll");
	}
	
	public void updateScroll() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		int sampleBegin = (int) (horizontalDisplacement / AudioChunkCanvas.GAP);
		int sampleEnd = sampleBegin + (size.x / AudioChunkCanvas.GAP);
		
		for (int i = 0; i < channelLinearLayout.getChildCount(); i++) {
			ChannelCanvas channelCanvas = (ChannelCanvas) channelLinearLayout.getChildAt(i);
			channelCanvas.setSampleBegin(sampleBegin);
			channelCanvas.setSampleEnd(sampleEnd);
			channelCanvas.invalidate();
		}
	}
	
	public void scrollVertically(int magnitude) {
		ScrollView scrollView = getVerticalScrollView();
		scrollView.scrollBy(0, magnitude);
	}
	
	public HorizontalScrollView getHorizontalScrollView() {
		return (HorizontalScrollView) getActivity().findViewById(R.id.channelHorizontalScrollView);
	}
	
	public ScrollView getVerticalScrollView() {
		return (ScrollView) getActivity().findViewById(R.id.channelScrollView);
	}
	
	public AudioChunk getChunkDragged() {
		return chunkDragged;
	}
	
	private void setIsDragging(boolean isDragging) {
		this.isDragging = isDragging;
	}
}
