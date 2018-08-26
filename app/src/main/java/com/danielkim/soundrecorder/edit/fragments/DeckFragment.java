package com.danielkim.soundrecorder.edit.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.Deck;
import com.danielkim.soundrecorder.edit.canvases.ChannelCanvas;
import com.danielkim.soundrecorder.edit.views.StationaryScrollView;

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
	
	private Deck deck;
	private LinearLayout channelLinearLayout;
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
		return v;
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
		for (int i = 0; i < channelLinearLayout.getChildCount(); i++) {
			if (i != selectedChannelIndex) {
				ChannelCanvas channelCanvas = (ChannelCanvas) channelLinearLayout.getChildAt(i);
				channelCanvas.disableCursor();
				channelCanvas.invalidate();
			}
		}
	}
	
	public void resize(int width, int height) {
		for (int i = 0; i < channelLinearLayout.getChildCount(); i++) {
			ChannelCanvas canvas = (ChannelCanvas) channelLinearLayout.getChildAt(i);
			canvas.resize(height);
		}
	}
	
	public void resize(int width) {
		resize(width, 300);
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
