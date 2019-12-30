/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_example_aubio_JniUtils */

#ifndef _Included_com_example_aubio_JniUtils
#define _Included_com_example_aubio_JniUtils
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_aubio_JniUtils
 * Method:    stringFromJNI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_aubio_JniUtils_stringFromJNI
  (JNIEnv *, jobject);

/*
 * Class:     com_example_aubio_JniUtils
 * Method:    add
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_example_aubio_JniUtils_add
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     com_example_aubio_JniUtils
 * Method:    sayHello
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_aubio_JniUtils_sayHello
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_example_aubio_JniUtils
 * Method:    increaseArrayEles
 * Signature: ([I)[I
 */
JNIEXPORT jintArray JNICALL Java_com_example_aubio_JniUtils_increaseArrayEles
  (JNIEnv *, jobject, jintArray);

/*
 * Class:     com_example_aubio_JniUtils
 * Method:    checkPwd
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_example_aubio_JniUtils_checkPwd
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif
