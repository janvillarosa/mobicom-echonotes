package com.mobicom.echonotes.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobicom.echonotes.R;
import com.mobicom.echonotes.data.RecordingSession;

public class RecordNote extends Activity {

	private static String mFileName, path;
	private boolean isRecording = false;

	private Uri fileUri;
	private MediaRecorder mRecorder = null;
	private ImageView startRecord, newPhoto, newText;
	private Button saveText, cancelText;
	private EditText noteName;
	RecordingSession currentNote;

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int MEDIA_TYPE_IMAGE = 1;
	private ViewStub stub;
	private Chronometer recordTime;
	private TextView numAnnotations;

	private Thread recordingThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recorder_screen);

		startRecord = (ImageView) findViewById(R.id.startRecordImageView);
		newPhoto = (ImageView) findViewById(R.id.newPhotoImageView);
		noteName = (EditText) findViewById(R.id.noteNameEditText);
		newText = (ImageView) findViewById(R.id.newTextNoteImageView);
		recordTime = (Chronometer) findViewById(R.id.recordTimeChronometer);
		numAnnotations = (TextView)findViewById(R.id.numAnnotationsTextView);

		newPhoto.setClickable(false);
		
		stub = (ViewStub) findViewById(R.id.stub);
		stub.setVisibility(View.INVISIBLE);
		//stub.inflate();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		setListeners();

		currentNote = new RecordingSession();

	}

	private void setListeners() {
		// RECORD BUTTON LISTENER
		startRecord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isRecording) {
					createDirectory();
					recordingThread = new Thread(new Runnable() {
						@Override
						public void run() {
							startRecording();
						}
					});

					recordingThread.start();
					
					recordTime.setBase(SystemClock.elapsedRealtime());
					recordTime.start();
					startRecord.setImageResource(R.drawable.stop_record);
					newPhoto.setClickable(true);
				} else {
					stopRecording();
					recordTime.stop();
					startRecord.setImageResource(R.drawable.start_record);

					currentNote.setName(mFileName);
					currentNote.setRecordingFilePath(path+"/"+ noteName.getText()
							.toString() + "_main_recording" + ".3gpp");
					newPhoto.setClickable(false);
					
					try {
						recordingThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					currentNote.writeMetadata();
				}
			}
		});

		// NEW PHOTO ANNOTATION LISTENER
		newPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent newPhotoIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				newPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

				startActivityForResult(newPhotoIntent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});

		// NEW TEXT ANNOTATION LISTENER
		newText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				stub.setVisibility(View.VISIBLE);

				saveText = (Button) findViewById(R.id.saveTextButton);

				saveText.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						currentNote.getAnnotationTimer().add(annotationTimestamp());
					}
				});

				cancelText = (Button) findViewById(R.id.cancelTextButton);

				cancelText.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						stub.setVisibility(View.INVISIBLE);
					}
				});

			}
		});
	}

	private void createDirectory() {
		File directory = new File(Environment.getExternalStorageDirectory()
				+ "/Echonotes/" + noteName.getText().toString());

		path = Environment.getExternalStorageDirectory() + "/Echonotes/"
				+ noteName.getText().toString();

		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				Log.d("Echonotes", "failed to create directory");
			}
		}
	}

	private Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private File getOutputMediaFile(int type) {

		// Create a media file name
		String timeStamp = Long.toString(annotationTimestamp());
		File mediaFile;

		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(path + "/IMG_" + timeStamp + ".jpg");

			currentNote.getListOfPicturePathAnnotations().add(mediaFile.getPath());
		} else {
			return null;
		}

		return mediaFile;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.recording_screen, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Annotation saved", Toast.LENGTH_SHORT).show();
				currentNote.getAnnotationTimer().add(annotationTimestamp());
				numAnnotations.setText(currentNote.getAnnotationTimer().size() + " annotations");
			} else if (resultCode == RESULT_CANCELED) {
			} else {
			}
		}
	}

	public long annotationTimestamp() {
		return SystemClock.elapsedRealtime() - recordTime.getBase();

	}

	private void startRecording() {

		File recordingFile = new File(path + "/" + noteName.getText().toString() + "_main_recording" + ".3gp");

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(recordingFile.getPath());
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		mRecorder.start();
		isRecording = true;
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		isRecording = false;
	}

}
