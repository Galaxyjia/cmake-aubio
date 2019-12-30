#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <jni.h>
#include "aubio.h"

static jfieldID getPtrFieldId(JNIEnv * env, jobject obj)
{
    static jfieldID ptrFieldId = 0;

    if (!ptrFieldId)
    {
        jclass c = (*env)->GetObjectClass(env, obj);
        ptrFieldId = (*env)->GetFieldID(env, c, "ptr", "J");
        (*env)->DeleteLocalRef(env, c);
    }

    return ptrFieldId;
}

static jfieldID getInputFieldId(JNIEnv * env, jobject obj)
{
    static jfieldID ptrFieldId = 0;

    if (!ptrFieldId)
    {
        jclass c = (*env)->GetObjectClass(env, obj);
        ptrFieldId = (*env)->GetFieldID(env, c, "input", "J");
        (*env)->DeleteLocalRef(env, c);
    }

    return ptrFieldId;
}

static jfieldID getOutputFieldId(JNIEnv * env, jobject obj)
{
    static jfieldID ptrFieldId = 0;
    if (!ptrFieldId)
    {
        jclass c = (*env)->GetObjectClass(env, obj);
        ptrFieldId = (*env)->GetFieldID(env, c, "output", "J");
        (*env)->DeleteLocalRef(env, c);
    }

    return ptrFieldId;
}


void Java_com_example_aubio_Notes_initNotes(JNIEnv * env, jobject obj, jint sampleRate, jint bufferSize, jint hopSize)
{
    unsigned int samplerate = (unsigned int) sampleRate; // samplerate
    unsigned int buf_size = (unsigned int) bufferSize; // window size
    unsigned int hop_size = (unsigned int) hopSize; // hop size


    aubio_notes_t * o = new_aubio_notes("default", buf_size, hop_size, samplerate);
    fvec_t *input = new_fvec (buf_size); // input buffer
    fvec_t *output = new_fvec (3);

    (*env)->SetLongField(env, obj, getPtrFieldId(env, obj), (jlong) (o));
    (*env)->SetLongField(env, obj, getInputFieldId(env, obj), (jlong) (input));
    (*env)->SetLongField(env, obj, getOutputFieldId(env, obj), (jlong) (output));
}

/**
 * @brief Get notes from buffer
 *
 * @param env
 * @param obj
 * @param inputArray
 * @return jfloatArray
 */
jfloatArray Java_com_example_aubio_Notes_getNotes(JNIEnv * env, jobject obj, jfloatArray inputArray)
{
    aubio_notes_t *ptr = (aubio_notes_t *) (*env)->GetLongField(env, obj, getPtrFieldId(env, obj));
    fvec_t *input = (fvec_t *) (*env)->GetLongField(env, obj, getInputFieldId(env, obj));
    fvec_t *output = (fvec_t *) (*env)->GetLongField(env, obj, getOutputFieldId(env, obj)); // input buffer

    jsize len = (*env)->GetArrayLength(env, inputArray);
    if(len != input->length) {
        return NULL;
    }

    jfloatArray ret_array=(*env)->NewFloatArray(env, 3);
    jfloat *body = (*env)->GetFloatArrayElements(env, inputArray, 0);
    // 1. copy inputArray to fvec_t* input (can be optimised)
    for(u_int i = 0; i < len; i++) {
        fvec_set_sample(input, body[i], i);
    }
    (*env)->ReleaseFloatArrayElements(env, inputArray, body, 0);


    aubio_notes_do(ptr, input, output);
    jfloat result[3]={0,0,0};

    // did we get a note off?
    if (output->data[2] != 0)
    {
        result[0]=output->data[0];
        result[1]=output->data[1];
        result[2]=output->data[2];
        //send_noteon(lastmidi, 0);
    }

    // did we get a note on?
    if (output->data[0] != 0)
    {
        result[0]=output->data[0];
        result[1]=output->data[1];
        result[2]=output->data[2];
        //send_noteon(lastmidi, output->data[1]);
    }

    (*env)->SetFloatArrayRegion(env, ret_array, 0, 3, result);

    return ret_array;
}

/**
 * @brief Cleanup notes, free memory
 *
 * @param env
 * @param obj
 */
void Java_com_example_aubio_Notes_cleanupNotes(JNIEnv * env, jobject obj)
{
    aubio_notes_t * o = (aubio_notes_t *) (*env)->GetLongField(env, obj, getPtrFieldId(env, obj));
    fvec_t *input = (fvec_t *) (*env)->GetLongField(env, obj, getInputFieldId(env, obj));
    fvec_t *output = (fvec_t *) (*env)->GetLongField(env, obj, getOutputFieldId(env, obj)); // input buffer
    del_aubio_notes (o);
    del_fvec (output);
    del_fvec (input);
    aubio_cleanup ();
}

