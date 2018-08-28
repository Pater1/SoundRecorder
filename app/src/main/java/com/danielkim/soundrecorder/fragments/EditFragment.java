package com.danielkim.soundrecorder.fragments;

import android.app.Activity;
//import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.AudioChunk;
import com.danielkim.soundrecorder.edit.AudioChunkInMemory;
import com.danielkim.soundrecorder.edit.Channel;
import com.danielkim.soundrecorder.edit.Deck;
import com.danielkim.soundrecorder.edit.canvases.OptionsJoystickCanvas;
import com.danielkim.soundrecorder.edit.editingoptions.MergeOption;
import com.danielkim.soundrecorder.edit.editingoptions.RenderAudioOption;
import com.danielkim.soundrecorder.edit.editingoptions.ScrollOption;
import com.danielkim.soundrecorder.edit.editingoptions.SplitTrimOption;
import com.danielkim.soundrecorder.edit.events.Event;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;

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
	
	public static final String DECK_FRAGMENT_TAG = "deck_fragment";
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_edit, container, false);
		
		// Add DeckFragment
		Deck d = genRandomDeck(getActivity());
		DeckFragment deckFragment = DeckFragment.newInstance(d);
//		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.deckLayout, deckFragment, DECK_FRAGMENT_TAG);
		transaction.commit();
//		getChildFragmentManager().executePendingTransactions();
		
		OptionsJoystickCanvas controlsJoystick = (OptionsJoystickCanvas) v.findViewById(R.id.controlsJoystick);
		controlsJoystick.addOption(new SplitTrimOption());
		controlsJoystick.addOption(new MergeOption());
		controlsJoystick.addOption(new RenderAudioOption(deckFragment));
		controlsJoystick.setDeckFragment(deckFragment);
		
		OptionsJoystickCanvas scrollJoystick = (OptionsJoystickCanvas) v.findViewById(R.id.scrollingJoystick);

		scrollJoystick.addOption(new ScrollOption(deckFragment, new Point(1, 0)));
		scrollJoystick.addOption(new ScrollOption(deckFragment, new Point(0, 1))); // switch in case of wrong direction
		scrollJoystick.addOption(new ScrollOption(deckFragment, new Point(-1, 0)));
		scrollJoystick.addOption(new ScrollOption(deckFragment, new Point(0, -1))); // switch in case of wrong direction
		scrollJoystick.setDeckFragment(deckFragment);
		return v;
	}
	
	// gen test data
	private AudioChunk genRandomAudioChunk() {
		Random rand = new Random();
		float[] memory = new float[rand.nextInt(100) + 50];
		for (int i = 0; i < memory.length; i++) {
			memory[i] = (rand.nextFloat() * 2) - 1; // -1.0 to 1.0 inclusive
		}
		return new AudioChunkInMemory(memory);
	}
	
	private Channel genRandomChannel() {
		Channel c = new Channel();
		Random gen = new Random();
		long sampleLength = 0;
		
		for (int i = 0; i < (gen.nextInt(6) + 3); i++) {
			AudioChunk chunk = genRandomAudioChunk();
			boolean shouldGenGap = gen.nextBoolean();
			
			if (shouldGenGap) {
				int gap = gen.nextInt(100) + 50;
				chunk.setStartIndex(sampleLength + gap);
			} else {
				chunk.setStartIndex(sampleLength);
			}
			sampleLength = chunk.getEndIndex();
			c.add(chunk);
		}
		
		return c;
	}
	
	private Deck genRandomDeck(Context c) {
		Deck d = new Deck(c);
		Random gen = new Random();
		
		for (int i = 0; i < (gen.nextInt(10) + 5); i++) {
			d.add(genRandomChannel());
		}
		
		Event.setPrimaryHandler(d);
		
		return d;
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
	
	public void resizeComponents() {
		FrameLayout container = (FrameLayout) getActivity().findViewById(R.id.container);

//		DeckFragment deckFragment = (DeckFragment) getChildFragmentManager().findFragmentByTag(DECK_FRAGMENT_TAG);
		DeckFragment deckFragment = (DeckFragment) getFragmentManager().findFragmentByTag(DECK_FRAGMENT_TAG);
		deckFragment.resizeMax();
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
