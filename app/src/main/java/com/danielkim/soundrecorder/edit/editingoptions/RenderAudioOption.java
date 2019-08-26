package com.danielkim.soundrecorder.edit.editingoptions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.danielkim.soundrecorder.R;
import com.danielkim.soundrecorder.edit.fragments.DeckFragment;

public class RenderAudioOption extends Option {
	
	private DeckFragment deckFragment;
	
	public RenderAudioOption(DeckFragment deckFragment) {
		this.deckFragment = deckFragment;
	}
	
	@Override
	protected boolean passedDownOnTouchUp(long[] cursorArray, int channelIndex) {
		final Context context = deckFragment.getActivity();
		
		AlertDialog.Builder renameFileBuilder = new AlertDialog.Builder(context);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_rename_file, null);
		
		final EditText input = (EditText) view.findViewById(R.id.new_name);
		
		renameFileBuilder.setTitle("Render");
		renameFileBuilder.setCancelable(true);
		renameFileBuilder.setPositiveButton(context.getString(R.string.dialog_action_ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							String value = input.getText().toString().trim();
							deckFragment.renderAudio(value);
						} catch (Exception e) {
//							Log.e(LOG_TAG, "exception", e);
						}
						
						dialog.cancel();
					}
				});
		renameFileBuilder.setNegativeButton(context.getString(R.string.dialog_action_cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		
		renameFileBuilder.setView(view);
		AlertDialog alert = renameFileBuilder.create();
		alert.show();
		return true;
	}
	
	@Override
	protected boolean passedDownOnTouchMove(long[] cursorArray, int channelIndex) {
		return false;
	}
	
	@Override
	protected boolean passedDownOnTouchDown(long[] cursorArray, int channelIndex) {
		return false;
	}
	
	@Override
	public int getColor() {
		return Color.CYAN;
	}

	@Override
	public Bitmap getIcon(Resources res){
		return BitmapFactory.decodeResource(res, R.drawable.save);
	}
}