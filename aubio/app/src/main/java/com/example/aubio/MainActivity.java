package com.example.aubio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.widget.TextView;

import java.util.logging.Logger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private int         sampleRate = 44100;
    private int         bufferSize = 4096;
    private short[]     intermediaryBuffer = null;

    /* These variables are used to store pointers to the C objects so JNI can keep track of them */
    private Notes notes=null;

    public boolean      isRecording = false;
    private AudioRecord audioRecord = null;
    Thread audioThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("onCreate");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    11);

        } else {
            System.out.println("onCreate!!");
            init();
            start();
        }

    }

    private void init() {
        int readSize = bufferSize / 4;
        intermediaryBuffer = new short[readSize];
        notes=new Notes(sampleRate, readSize);
        System.out.println("init");
    }


    public void start() {
        if(!isRecording) {
            isRecording = true;
//        sampleRate = AudioUtils.getSampleRate();
//        bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, sampleRate, AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            audioRecord.startRecording();
            System.out.println("start");
            audioThread = new Thread(new Runnable() {
                //Runs off the UI thread
                @Override
                public void run() {
                    findNote();
                }
            }, "Tuner Thread");
            audioThread.start();
        }
    }

    private void findNote() {
        while (isRecording) {
            int amountRead = audioRecord.read(intermediaryBuffer, 0, bufferSize / 4);

            final float[] results = notes.get(intermediaryBuffer);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView pitchView=(TextView) findViewById(R.id.pitchView);
                    if(results!=null && results[0]!=0){
                        String text=String.valueOf(results[0])+","
                                +String.valueOf(results[1])+","
                                +String.valueOf(results[2]);
                        System.out.println(text);
                        pitchView.setText(text);
                    }

                }
            });
        }
    }

}

