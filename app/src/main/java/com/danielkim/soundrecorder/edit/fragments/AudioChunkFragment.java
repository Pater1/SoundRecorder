package com.danielkim.soundrecorder.edit.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableRow;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.canvases.AudioChunkCanvas;

/**
 * Fragment to display an AudioChunk.
 * Use the {@link AudioChunkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioChunkFragment extends Fragment {
	
	private AudioChunk chunk;
	private AudioChunkCanvas canvas;
	
	private OnFragmentInteractionListener mListener;
	
	public AudioChunkFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment AudioChunkFragment.
	 */
	public static AudioChunkFragment newInstance(AudioChunk chunk) {
		AudioChunkFragment fragment = new AudioChunkFragment();
		Bundle args = new Bundle();
		fragment.chunk = chunk;
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_audio_chunk, container, false);
		FrameLayout layout = (FrameLayout) v.findViewById(R.id.canvasLayout);
		canvas = new AudioChunkCanvas(getActivity(), chunk);
		layout.addView(canvas);
		
		return v;
	}
	
	public void resizeCanvas(int width, int height) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) canvas.getLayoutParams();
		params.width = width;
		params.height = height;
		canvas.setLayoutParams(params);
	}
	
	public void resizeCanvas(int width) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) canvas.getLayoutParams();
		params.width = width;
		canvas.setLayoutParams(params);
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
