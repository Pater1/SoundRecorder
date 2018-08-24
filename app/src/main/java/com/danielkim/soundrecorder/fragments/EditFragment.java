package com.danielkim.soundrecorder.fragments;

import android.app.Activity;
//import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.AudioChunkInMemory;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.fragments.AudioChunkFragment;
import com.danielkim.soundrecorder.edit.fragments.ChannelFragment;

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
	
	public static final String CHANNEL_FRAGMENT_TAG = "channel_fragment";
	
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
//		TableRow canvasTableRow = (TableRow) v.findViewById(R.id.channelTableRow);
		
		// test AudioChunk
//		AudioChunkFragment chunkFragment = AudioChunkFragment.newInstance(genRandomAudioChunk());
//		android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.add(R.id.channelLayout, chunkFragment, CHANNEL_FRAGMENT_TAG);
//		transaction.commit();
		
		// test Channel
		ChannelFragment channelFragment = ChannelFragment.newInstance(genRandomChannel());
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.channelLayout, channelFragment, CHANNEL_FRAGMENT_TAG);
		transaction.commit();
		return v;
	}
	
	// gen test data
	private AudioChunk genRandomAudioChunk() {
		Random gen = new Random();
		int len = gen.nextInt(500) + 500;
		float[] pcm = new float[len];
		for (int i = 0; i < pcm.length; i++) {
			pcm[i] = gen.nextFloat() * 1000;
		}
		return new AudioChunkInMemory(pcm);
	}
	
	private Channel genRandomChannel() {
		Channel c = new Channel();
		Random gen = new Random();
		long sampleLength = 0;
		
		for (int i = 0; i < (gen.nextInt(4) + 2); i++) {
			AudioChunk chunk = genRandomAudioChunk();
			chunk.setStartIndex(sampleLength);
			sampleLength = chunk.getEndIndex();
			c.add(chunk);
		}
		
		return c;
	}
	// end gen test data
	
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
