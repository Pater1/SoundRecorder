package com.danielkim.soundrecorder.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.AudioChunkInMemory;
import com.danielkim.soundrecorder.edit.canvases.AudioChunkCanvas;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment {
	
	private OnFragmentInteractionListener mListener;
	
	public EditFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment EditFragment.
	 */
	public static EditFragment newInstance() {
		EditFragment fragment = new EditFragment();
		Bundle args = new Bundle();
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
		View v = inflater.inflate(R.layout.fragment_edit, container, false);
		TableRow canvasTableRow = (TableRow) v.findViewById(R.id.channelTableRow);
		
		// test
		Random gen = new Random();
		float[] pcm = new float[100];
		for (int i = 0; i < pcm.length; i++) {
			pcm[i] = gen.nextFloat() * 1000;
		}
		AudioChunk chunk = new AudioChunkInMemory(pcm);
		AudioChunkCanvas canvas = new AudioChunkCanvas(getActivity(), chunk);
		canvasTableRow.addView(canvas);
		
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
