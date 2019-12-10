package com.example.aubio;

public class Notes {

    public long ptr = 0;
    public long input = 0;
    public long output = 0;
    
    
    public Notes(int sampleRate, int bufferSize, int hopSize){
        this.initNotes(sampleRate, bufferSize, hopSize);
    }
    
    public Notes(int sampleRate, int bufferSize){
        this.initNotes(sampleRate, bufferSize, 256);
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
        return this.getNotes(buffer);
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

    private native float[]    getNotes(float[] input);
    private native void     initNotes(int sampleRate, int bufferSize, int hopSize);
    private native void     cleanupNotes();

}
