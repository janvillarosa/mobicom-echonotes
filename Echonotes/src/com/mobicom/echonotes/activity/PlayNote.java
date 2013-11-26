package com.mobicom.echonotes.activity;

import java.io.IOException;

import com.mobicom.echonotes.R;
import com.mobicom.echonotes.data.RecordingSession;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class PlayNote extends Activity{
	
	private static String mFileName, path;

	private Uri fileUri;
	private MediaPlayer mPlayer = null;
	private ImageView playButton;
	private EditText noteName;
	RecordingSession currentNote;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recorder_screen);
		
		playButton = (ImageView)findViewById(R.id.playRecordingImageView);
		noteName = (EditText)findViewById(R.id.noteNameEditText);
		
		playButton.setOnClickListener(new OnClickListener() {
			
			boolean mStartPlaying = true;
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    playButton.setImageResource(R.drawable.stop_record);
                } else {
                    playButton.setImageResource(R.drawable.start_record);
                }
                mStartPlaying = !mStartPlaying;
            }
        });
		
	}
	
	private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

}
