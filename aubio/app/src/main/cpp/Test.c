//
// Created by 郭佳 on 2019-12-11.
//

#include "com_example_aubio_JniUtils.h"
#import <string.h>

//jstring Java_com_example_aubio_JniUtils_stringFromJNI
//        (JNIEnv *env, jobject jobj){
//
//};

jint Java_com_example_aubio_JniUtils_add
        (JNIEnv *env, jobject jobj, jint ji, jint jj){
    int result = ji + jj;
    return result;
};

JNIEXPORT jintArray JNICALL Java_com_example_aubio_JniUtils_increaseArrayEles
        (JNIEnv *env, jobject jobj, jintArray jarray){
    jsize size = (*env)->GetArrayLength(env,jarray);
    jint* intArray = (*env)->GetIntArrayElements(env,jarray,JNI_FALSE);
    int i;
    for(i=0;i<size;i++){
        *(intArray+i) +=10;
    }
    return jarray;
};


