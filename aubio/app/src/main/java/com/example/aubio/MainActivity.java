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
        int hopSize = 256;
        float notesSilence = -70.0f;
        float notesReleaseDrop = 10.0f;
        float centPrecision = 1.0f;
        float notesMinioiMs = 15.0f;
        int median = 3;

        notes=new Notes(sampleRate, readSize ,hopSize, notesSilence, notesReleaseDrop, centPrecision, notesMinioiMs, median);
        TextView configView=(TextView) findViewById(R.id.configView);
        String configtext = "sampleRate: "+String.valueOf(sampleRate)+"\n"
                            +"readSize: "+String.valueOf(readSize)+"\n"
                            +"hopSize: "+String.valueOf(hopSize)+"\n"
                            +"notesSilence: "+String.valueOf(notesSilence)+"\n"
                            +"notesReleaseDrop: "+String.valueOf(notesReleaseDrop)+"\n"
                            +"centPrecision: "+String.valueOf(centPrecision)+"\n"
                            +"notesMinioiMs: "+String.valueOf(notesMinioiMs)+"\n"
                            +"median: "+String.valueOf(median)+"\n";

        configView.setText(configtext);

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
                        String text="note: "+String.valueOf(results[0])+"\n"
                                    +"velocity: "+String.valueOf(results[1])+"\n"
                                    +"lastnote: "+String.valueOf(results[2]);
                        System.out.println(text);
                        pitchView.setText(text);
                    }
                }
            });
        }
    }

}

