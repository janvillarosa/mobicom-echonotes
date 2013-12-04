package com.mobicom.echonotes.activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mobicom.echonotes.R;
import com.mobicom.echonotes.database.Annotation;
import com.mobicom.echonotes.database.DatabaseHelper;

public class PlayNote extends Activity {

	private MediaPlayer mPlayer;
	private ImageView playButton, image, nextAnnotation, previousAnnotation;
	private TextView noteNameTextView;
	private TextView numAnnotationsTextView;
	private SeekBar seekbar;
	private int numAnnotations, annotationIterator = 0;
	private boolean mStartPlaying = true;
	private Handler handler = new Handler();
	private boolean playStart = false;
	private ArrayList<Annotation> annotations;
	private View textStub, imageStub;

	private Runnable moveSeekBarRunnable = new Runnable() {
		@Override
		public void run() {
			seekbar.setProgress(mPlayer.getCurrentPosition());
			showAnnotations();
			handler.postDelayed(moveSeekBarRunnable, 100);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_screen);

		DatabaseHelper db = new DatabaseHelper(getApplicationContext());

		playButton = (ImageView) findViewById(R.id.playRecordingImageView);
		nextAnnotation = (ImageView) findViewById(R.id.nextAnnotation);
		previousAnnotation = (ImageView) findViewById(R.id.previousAnnotation);

		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		noteNameTextView = (TextView) findViewById(R.id.noteNameTextView);
		numAnnotationsTextView = (TextView) findViewById(R.id.numAnnotations);
		mPlayer = new MediaPlayer();

		textStub = ((ViewStub) findViewById(R.id.playerTextStub)).inflate();
		imageStub = ((ViewStub) findViewById(R.id.playerImageStub)).inflate();
		textStub.setVisibility(View.GONE);
		imageStub.setVisibility(View.GONE);

		Bundle extras;

		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
			} else {
				numAnnotations = extras.getInt("NUM_ANNOTATIONS");
				noteNameTextView.setText(extras.getString("NOTE_NAME"));
			}
		} else {
			noteNameTextView.setText((String) savedInstanceState
					.getSerializable("NOTE_NAME"));
			numAnnotations = (Integer) savedInstanceState
					.getSerializable("NUM_ANNOTATIONS");
			numAnnotationsTextView.setText(numAnnotations + " annotations");
		}

		annotations = db.getAnnotationsOfNote(noteNameTextView.getText()
				.toString());
		numAnnotationsTextView.setText(numAnnotations + " annotations");

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (fromUser) {
					mPlayer.seekTo(progress);
					seekbar.setProgress(progress);
					showAnnotations();
				} else {
					seekbar.setProgress(mPlayer.getCurrentPosition());
					showAnnotations();
				}
			}
		});

		handler.removeCallbacks(moveSeekBarRunnable);

		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onPlay(mStartPlaying);
				if (mStartPlaying) {
					playButton.setImageResource(R.drawable.ic_action_pause);
				} else {
					playButton.setImageResource(R.drawable.ic_action_play);
				}
				mStartPlaying = !mStartPlaying;
			}
		});

		nextAnnotation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (annotationIterator != annotations.size()) {
					int timeStamp = Integer.parseInt(annotations.get(
							annotationIterator).getAnnotationTimeStamp());
					mPlayer.seekTo(timeStamp);
					seekbar.setProgress(timeStamp);
				} else {
					nextAnnotation.setClickable(false);
				}
			}
		});
		previousAnnotation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				annotationIterator -= 2;
				if (annotationIterator!=0 || annotationIterator!=1) {
				int timeStamp = Integer.parseInt(annotations.get(
						annotationIterator).getAnnotationTimeStamp());
				mPlayer.seekTo(timeStamp);
				seekbar.setProgress(timeStamp);
				} else {
					previousAnnotation.setClickable(false);
				}
			}
		});

	}

	private void onPlay(boolean start) {
		if (start) {
			startPlaying();
		} else {
			pausePlaying();
		}
	}

	private void startPlaying() {
		if (!playStart) {
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(Environment.getExternalStorageDirectory()
						+ "/Echonotes/" + noteNameTextView.getText().toString()
						+ "/" + noteNameTextView.getText().toString()
						+ "_main_recording.3gp");
				mPlayer.prepare();

				seekbar.setProgress(0);
				seekbar.setMax(mPlayer.getDuration());

				handler.post(moveSeekBarRunnable);

				mPlayer.start();
				playStart = true;
			} catch (IOException e) {

			}
		} else {
			mPlayer.start();
		}
	}

	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void pausePlaying() {
		mPlayer.pause();
	}

	private void showAnnotations() {
		try {
			Annotation annotationInView = annotations.get(annotationIterator);

			if (mPlayer.getCurrentPosition() >= Integer
					.parseInt(annotationInView.getAnnotationTimeStamp())) {
				if (annotationInView.getAnnotationType().equals("text")) {

					String temp = "";
					String curr;

					try {
						BufferedReader reader = new BufferedReader(
								new FileReader(
										annotationInView
												.getAnnotationFilePath()));

						while ((curr = reader.readLine()) != null) {
							temp += curr;
							temp += "\n";
						}
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					TextView textAnnotation = (TextView) findViewById(R.id.textAnnotationShowTextView);
					textAnnotation.setText(temp);

					textStub.setVisibility(View.VISIBLE);
					imageStub.setVisibility(View.GONE);
					Animation animate1 = AnimationUtils.makeInAnimation(
							getApplicationContext(), true);
					animate1.setDuration(100);
					textStub.startAnimation(animate1);
					annotationIterator++;
				} else {

					ImageView imageAnnotation = (ImageView) findViewById(R.id.imageAnnotationImageView);

					Bitmap myBitmap = BitmapFactory.decodeFile(annotations.get(
							annotationIterator).getAnnotationFilePath());
					imageAnnotation.setImageBitmap(myBitmap);

					imageStub.setVisibility(View.VISIBLE);
					textStub.setVisibility(View.GONE);
					Animation animate2 = AnimationUtils.makeInAnimation(
							getApplicationContext(), true);
					animate2.setDuration(100);
					imageStub.startAnimation(animate2);
					annotationIterator++;
				}
			}
		} catch (Exception e) {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				image.setImageBitmap(thumbnail);
			}
		}
	}

	protected void onPause() {
		super.onPause();
		pausePlaying();

	}

	protected void onDestroy() {
		super.onDestroy();
		stopPlaying();
		handler.removeCallbacks(moveSeekBarRunnable);
		moveSeekBarRunnable = null;
		handler = null;
	}

}