package com.danielkim.soundrecorder.edit.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.canvases.AudioChunkCanvas;
import com.danielkim.soundrecorder.edit.canvases.ChannelCanvas;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChannelFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChannelFragment extends Fragment {

//	public static final String BASE_AUDIO_CHUNK_TAG = "audio_chunk_frag";
	
	private Channel channel;
	//	private int chunkCount;
	private OnFragmentInteractionListener mListener;
	
	public ChannelFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment ChannelFragment.
	 */
	public static ChannelFragment newInstance(Channel channel) {
		ChannelFragment fragment = new ChannelFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		fragment.channel = channel;
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_channel, container, false);
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//		for (int i = 0; i < channel.getDataSize(); i++) {
//			AudioChunkFragment chunkFragment = AudioChunkFragment.newInstance(channel.getChunk(i));
//			transaction.add(R.id.audioChunkCanvasFragmentLayout, chunkFragment, BASE_AUDIO_CHUNK_TAG + chunkCount++);
//		}
//
//		transaction.commit();
		
		ChannelCanvas canvas = new ChannelCanvas(getActivity(), channel);
		FrameLayout canvasLayout = (FrameLayout) v.findViewById(R.id.channelCanvasFragmentLayout);
		canvasLayout.addView(canvas);
		
		return v;
	}
	
	public void resize(int width) {
//		FragmentManager fm = getFragmentManager();
//
//		for (Fragment f : fm.getFragments()) {
//			AudioChunkFragment audioChunkFragment = (AudioChunkFragment) f;
//			audioChunkFragment.resizeCanvas(width);
//		}
		
		FrameLayout channelLayout = (FrameLayout) getActivity().findViewById(R.id.channelCanvasFragmentLayout);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) channelLayout.getLayoutParams();
		params.width = width;
		channelLayout.setLayoutParams(params);
		Log.d("channel_layout_height", channelLayout.getHeight() + "");
		Log.d("channel_layout_width", channelLayout.getWidth() + "");
//		Log.d()
		ChannelCanvas canvas = (ChannelCanvas) channelLayout.getChildAt(0);
		canvas.resize(width, channelLayout.getHeight());
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
