package com.mobicom.echonotes.activity;

import java.io.IOException;

import com.mobicom.echonotes.R;
import com.mobicom.echonotes.data.RecordingSession;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayNote extends Activity{

	private MediaPlayer mPlayer;
	private ImageView playButton;
	private TextView noteNameTextView;
	private TextView numAnnotationsTextView;
	private SeekBar seekbar;
	private int numAnnotations;
	private ImageView image;
	private RecordingSession currentNote;
	private Handler handler = new Handler();
    
    //THREAD
    private Runnable moveSeekBarThread = new Runnable() {
        @Override
		public void run() {
                if(mPlayer.isPlaying()){

                int mediaPos_new = mPlayer.getCurrentPosition();
                int mediaMax_new = mPlayer.getDuration();
                seekbar.setMax(mediaMax_new);
                seekbar.setProgress(mediaPos_new);

                handler.postDelayed(this, 100); 
        }

        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_screen);
		
		playButton = (ImageView)findViewById(R.id.playRecordingImageView);
		seekbar = (SeekBar)findViewById(R.id.seekBar1);
		noteNameTextView = (TextView)findViewById(R.id.noteNameTextView);
		numAnnotationsTextView = (TextView)findViewById(R.id.numAnnotations);
		currentNote = new RecordingSession();
		mPlayer = new MediaPlayer();
		
		Bundle extras;
		
		if (savedInstanceState == null) {
		    extras = getIntent().getExtras();
		    if(extras == null) {
		    } else {
		    	numAnnotations = extras.getInt("NUM_ANNOTATIONS");
		        noteNameTextView.setText(extras.getString("NOTE_NAME"));
		        numAnnotationsTextView.setText( numAnnotations + " annotations");
		    }
		} else {
		    noteNameTextView.setText((String) savedInstanceState.getSerializable("NOTE_NAME"));
		    numAnnotations = (Integer) savedInstanceState.getSerializable("NUM_ANNOTATIONS");
	        numAnnotationsTextView.setText( numAnnotations + " annotations");
		}

		/*seekbar.setMax(mediaMax); // Set the Maximum range of the
		seekbar.setProgress(mediaPos);// set current progress to song's

		handler.removeCallbacks(moveSeekBarThread);
		handler.postDelayed(moveSeekBarThread, 100);*/
	
		
		playButton.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	boolean mStartPlaying = true;
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
            mPlayer.setDataSource(currentNote.getRecordingFilePath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
        case 0:
            if(resultCode==RESULT_OK){
               Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
               image.setImageBitmap(thumbnail);
                }
        }
    }


}