#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_demo_lucius_baselib_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


/**
 * 将byte数组使用MD5进行加密
 * @param env
 * @param source
 * @return
 */
static jstring ToMD5(JNIEnv *env,jbyteArray source){

    //MessageDigest class
    jclass classMessageDigest=env->FindClass("java/security/MessageDigest");

    //MessageDigest.getInsatance()
    jmethodID midGetInstance=env->GetStaticMethodID(classMessageDigest,"getInstance",
                                                    "(Ljava/lang/String;)Ljava/security/MessageDigest;");

    //MessageDigest object
   jobject objMessageDigest=env->CallStaticObjectMethod(classMessageDigest,midGetInstance,env->NewStringUTF("md5"));

   jmethodID midUpdate=env->GetMethodID(classMessageDigest, "update", "([B)V");
   env->CallVoidMethod(objMessageDigest,midUpdate,source);

   //Digst
   jmethodID midDigest=env->GetMethodID(classMessageDigest,"digest", "()[B");

   jbyteArray objArraySign=(jbyteArray)env->CallObjectMethod(objMessageDigest,midDigest);

   jsize intArrayLength=env->GetArrayLength(objArraySign);
   jbyte *byte_array_elements=env->GetByteArrayElements(objArraySign,NULL);

   size_t length=(size_t)intArrayLength*2+1;
   char *char_result=(char *)malloc(length);

    //memset用来对一段内存空间全部设置为某个字符
    memset(char_result, 0, length);




}