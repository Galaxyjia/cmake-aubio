package com.example.aubio;

public class Notes {

    public long ptr = 0;
    public long input = 0;
    public long output = 0;
    
    public Notes(int sampleRate, int bufferSize){
        this.initNotes(sampleRate, bufferSize, 256, -70.0f, 10.0f, 1.0f, 30.0f, 6);
    }

    public Notes(int sampleRate, int bufferSize, int hopSize){
        this.initNotes(sampleRate, bufferSize, hopSize, -70.0f, 10.0f, 1.0f, 30.0f, 6);
    }

    public Notes(
        int sampleRate, int bufferSize, int hopSize, 
        float notesSilence
    ){
        this.initNotes(
            sampleRate, bufferSize, hopSize, 
            notesSilence, 10.0f, 1.0f, 30.0f, 6
        );
    }

    public Notes(
        int sampleRate, int bufferSize, int hopSize, 
        float notesSilence, 
        float notesReleaseDrop
    ){
        this.initNotes(
            sampleRate, bufferSize, hopSize, 
            notesSilence, notesReleaseDrop, 
            1.0f, 30.0f, 6
        );
    }

    public Notes(
        int sampleRate, int bufferSize, int hopSize, 
        float notesSilence, 
        float notesReleaseDrop,
        float centPrecision
    ){
        this.initNotes(
            sampleRate, bufferSize, hopSize, 
            notesSilence, notesReleaseDrop, 
            centPrecision, 30.0f, 6
        );
    }

    public Notes(
        int sampleRate, int bufferSize, int hopSize, 
        float notesSilence, 
        float notesReleaseDrop,
        float centPrecision,
        float notesMinioiMs
    ){
        this.initNotes(
            sampleRate, bufferSize, hopSize, 
            notesSilence, notesReleaseDrop, 
            centPrecision, notesMinioiMs, 6
        );
    }
    
    public Notes(
        int sampleRate, int bufferSize, int hopSize, 
        float notesSilence, 
        float notesReleaseDrop,
        float centPrecision,
        float notesMinioiMs,
        int median
    ){
        this.initNotes(
            sampleRate, bufferSize, hopSize, 
            notesSilence, notesReleaseDrop, 
            centPrecision, notesMinioiMs, median
        );
    }

    /**
     *
     * @param input Input Buffer
     * @return return array contain three elements
     * 0. the midi note value, or 0 if no note was found
     * 1. the note velocity
     * 2. the midi note to turn off
     */
    public float[] get(short[] input){
        float[] buffer=shortArrayToFloatArray(input);
        return this.getNotes(buffer, 45, 85);
    }

    public float[] get(short[] input, int minNote){
        float[] buffer=shortArrayToFloatArray(input);
        return this.getNotes(buffer, minNote, 85);
    }

    public float[] get(short[] input, int minNote, int maxNote){
        float[] buffer=shortArrayToFloatArray(input);
        return this.getNotes(buffer, minNote, maxNote);
    }

    public void cleanup(){
        this.cleanupNotes();
    }

    private float[] shortArrayToFloatArray(short[] array) {
        float[] fArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            fArray[i] = (float) array[i];
        }
        return fArray;
    }

    static {
        System.loadLibrary("aubio");
    }

    private native void initNotes(
            int sampleRate,
            int bufferSize,
            int hopSize,
            float notesSilence,
            float notesReleaseDrop,
            float centPrecision,
            float notesMinioiMs,
            int median
    );
    private native float[] getNotes(float[] input, int minNote, int maxNote);
    private native void     cleanupNotes();

}
