package com.example.aubio;
//
public class JniUtils {
//
    static {
        System.loadLibrary("aubio");
    //        System.loadLibrary("pitch");
    }

    public native String  stringFromJNI();

    public native int add(int x, int y);

    public native String sayHello(String s);

    public native int[] increaseArrayEles(int[] intArray);

    public  native int checkPwd(String pwd);
}
