package com.danielkim.soundrecorder.edit.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.Deck;
import com.danielkim.soundrecorder.edit.canvases.ChannelCanvas;
import com.danielkim.soundrecorder.edit.canvases.DeckCursorCanvas;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeckFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeckFragment extends Fragment {
	
	private static final int MARGIN = 10;
	private static final int CHANNEL_HEIGHT = 300;
	
	private Deck deck;
	private LinearLayout channelLinearLayout;
	private int cursorChannelIndex;
	private int greatestChannelLength;
	private OnFragmentInteractionListener mListener;
	
	public DeckFragment() {
		// Required empty public constructor
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_deck, container, false);
		channelLinearLayout = (LinearLayout) v.findViewById(R.id.channelLinearLayout);
		updateDeckView();
//		DeckCursorCanvas deckCursorCanvas = (DeckCursorCanvas) v.findViewById(R.id.deckCursorCanvas);
//		Toast.makeText(getActivity(), channelLinearLayout.getLayoutParams().width + " lw", Toast.LENGTH_SHORT).show();
//		Toast.makeText(getActivity(), channelLinearLayout.getLayoutParams().width + " w", Toast.LENGTH_SHORT).show();
//		Toast.makeText(getActivity(), channelLinearLayout.getLayoutParams().height + " h", Toast.LENGTH_SHORT).show();
		return v;
	}
	
	private void updateDeckView() {
		if (deck != null) {
			channelLinearLayout.removeAllViews();
			
			int curIndex = 0;
			Channel curChannel;
			
			while ((curChannel = deck.getChannel(curIndex)) != null) {
				ChannelCanvas channelCanvas = new ChannelCanvas(getActivity(), curChannel, curIndex, this);
				channelLinearLayout.addView(channelCanvas);
				
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) channelCanvas.getLayoutParams();
				params.bottomMargin = MARGIN;
				params.topMargin = MARGIN;
				params.leftMargin = MARGIN;
				params.rightMargin = MARGIN;
				channelCanvas.setLayoutParams(params);
				curIndex++;
			}
		}
	}
	
	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);
//		if (context instanceof OnFragmentInteractionListener) {
//			mListener = (OnFragmentInteractionListener) context;
//		} else {
//			throw new RuntimeException(context.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	public void updateCursor(int selectedChannelIndex) {
		if (selectedChannelIndex != cursorChannelIndex) {
			ChannelCanvas prevCursorChannel = ((ChannelCanvas) channelLinearLayout.getChildAt(cursorChannelIndex));
			prevCursorChannel.disableCursor();
			prevCursorChannel.invalidate();
			cursorChannelIndex = selectedChannelIndex;
		}
	}
	
	public long[] getCursorPoints() {
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
			if (canvas.getLayoutParams().width < width) {
				canvas.resize(width, CHANNEL_HEIGHT);
				greatestChannelLength = Math.max(canvas.getLayoutParams().width, greatestChannelLength);
			}
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
	
	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
