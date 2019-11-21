#include <jni.h>
#include <string>



extern "C"
const char *AUTH_KEY="MySecretSignature";
const char *PACKAGE_NAME="demo.lucius.baselib";
const char *RELEASE_SIGN_MD5="";


extern "C" JNIEXPORT jstring JNICALL
Java_demo_lucius_baselib_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}



static jobject getApplication(JNIEnv *env) {

    jobject application = NULL;
    jclass activity_thread_clz=env->FindClass("android/app/ActivityThread");
    if (activity_thread_clz!=NULL){
        //得到ActivtyThread的currentApplication静态方法id
        jmethodID currentApplication=env->GetStaticMethodID(activity_thread_clz,"currentApplication", "()Landroid/app/Application;");
        if(currentApplication!=NULL){
            application=env->CallStaticObjectMethod(activity_thread_clz,currentApplication);
        } else{
            printf("未能获取Applicaion");
        }
        env->DeleteLocalRef(activity_thread_clz);
    }

    return application;


}

//将16进制数转换为String
static void toHexStr(const char *source, char *dest,int sourceLength){
    short i;
    char highByte,lowByte;

    for (int i = 0; i <sourceLength ; i++) {
        highByte=source[i]>>4;
        lowByte=(char)(source[i]&0x0f);
        highByte+=0x30;

        if(highByte>0x39){
            dest[i*2]=(char)(highByte+0x07);
        } else{
            dest[i*2]=highByte;
        }

        lowByte+=30;
        if(lowByte>0x39){
            dest[i*2+1]=(char)(lowByte+0x07);
        } else{
            dest[i*2+1]=lowByte;
        }
    }

}


/**
 * 将byte数组使用MD5进行加密
 * @param env
 * @param source
 * @return
 */
static jstring ToMD5(JNIEnv *env, jbyteArray source) {

    //MessageDigest class
    jclass classMessageDigest = env->FindClass("java/security/MessageDigest");

    //MessageDigest.getInsatance()
    jmethodID midGetInstance = env->GetStaticMethodID(classMessageDigest, "getInstance",
                                                      "(Ljava/lang/String;)Ljava/security/MessageDigest;");

    //MessageDigest object
    jobject objMessageDigest = env->CallStaticObjectMethod(classMessageDigest, midGetInstance,
                                                           env->NewStringUTF("md5"));

    jmethodID midUpdate = env->GetMethodID(classMessageDigest, "update", "([B)V");
    env->CallVoidMethod(objMessageDigest, midUpdate, source);

    //Digst
    jmethodID midDigest = env->GetMethodID(classMessageDigest, "digest", "()[B");

    jbyteArray objArraySign = (jbyteArray) env->CallObjectMethod(objMessageDigest, midDigest);

    jsize intArrayLength = env->GetArrayLength(objArraySign);
    jbyte *byte_array_elements = env->GetByteArrayElements(objArraySign, NULL);

    size_t length = (size_t) intArrayLength * 2 + 1;
    char *char_result = (char *) malloc(length);

    //memset用来对一段内存空间全部设置为某个字符
    memset(char_result, 0, length);

    toHexStr((const char *)byte_array_elements,char_result,intArrayLength);

    //在末尾补\0;
    *(char_result+intArrayLength*2)+='\0';
    jstring stringResult=env->NewStringUTF(char_result);
    //release
    env->ReleaseByteArrayElements(objArraySign,byte_array_elements,JNI_ABORT);
    //指针
    free(char_result);
    return stringResult;

}




extern "C"
JNIEXPORT jstring JNICALL
Java_demo_lucius_baselib_MainActivity_getSignature(JNIEnv *env, jobject instance) {

    // TODO
    jobject context=getApplication(env);
    //获取Context类
    jclass cls=env->GetObjectClass(context);
    //得到getPackageManager方法的ID
    jmethodID mid=env->GetMethodID(cls, "getPackageManager",
                                        "()Landroid/content/pm/PackageManager;");

    //获取应用包的管理器
    jobject pm=env->CallObjectMethod(context,mid);

    //得到getPackageName方法的ID
    mid=env->GetMethodID(cls,"getPackageName", "()Ljava/lang/String;");
    //获取当前应用包名
    jstring packageName=(jstring)env->CallObjectMethod(context,mid);

    //将String类型，直接转换为C的字符串
    const char* c_pack_name=env->GetStringUTFChars(packageName,NULL);

    //比较包名，如果不一致，直接return包名
    if (strcmp(c_pack_name,PACKAGE_NAME)){
        return (env)->NewStringUTF(c_pack_name);
    }

    //获得PackageManager类
    cls=env->GetObjectClass(cls);
    //得到getPackageInfo方法的ID
    mid=env->GetMethodID(cls, "getPackageInfo",
                         "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

    //获取应用包信息
    jobject packageInfo=env->CallObjectMethod(pm,mid,packageName,0x40); //GET_SIGNATURE=64;
    //获取packageInfo类
    cls=env->GetObjectClass(packageInfo);
    //获取签名数组属性的ID
    jfieldID fid=env->GetFieldID(cls,"signatures", "[Landroid/content/pm/Signature;");
    //得到签名数组
    jobjectArray  signatures=(jobjectArray)env->GetObjectField(packageName,fid);
    //得到签名
    jobject signature=env->GetObjectArrayElement(signatures,0);
    //获得Signature类
    cls=env->GetObjectClass(signature);
    mid = env->GetMethodID(cls, "toByteArray", "()[B");

    //当前应用签名信息
    jbyteArray signatureByteArray=(jbyteArray)env->CallObjectMethod(signature,mid);
    //转化为jstring
    jstring str=ToMD5(env,signatureByteArray);
    char *c_msg=(char *)env->GetStringUTFChars(str,0);

    if (strcmp(c_msg, RELEASE_SIGN_MD5) == 0) {
        return (env)->NewStringUTF(AUTH_KEY);
    } else {
        return (env)->NewStringUTF(c_msg);
    }
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_demo_lucius_baselib_ui_CheckSignatureActivity_getSignature(JNIEnv *env, jobject instance) {

    // TODO
    // TODO
    jobject context=getApplication(env);
    //获取Context类
    jclass cls=env->GetObjectClass(context);
    //得到getPackageManager方法的ID
    jmethodID mid=env->GetMethodID(cls, "getPackageManager",
                                   "()Landroid/content/pm/PackageManager;");

    //获取应用包的管理器
    jobject pm=env->CallObjectMethod(context,mid);

    //得到getPackageName方法的ID
    mid=env->GetMethodID(cls,"getPackageName", "()Ljava/lang/String;");
    //获取当前应用包名
    jstring packageName=(jstring)env->CallObjectMethod(context,mid);

    //将String类型，直接转换为C的字符串
    const char* c_pack_name=env->GetStringUTFChars(packageName,NULL);

    //比较包名，如果不一致，直接return包名
    if (strcmp(c_pack_name,PACKAGE_NAME)){
        return (env)->NewStringUTF(c_pack_name);
    }

    //获得PackageManager类
    cls=env->GetObjectClass(cls);
    //得到getPackageInfo方法的ID
    mid=env->GetMethodID(cls, "getPackageInfo",
                         "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

    //获取应用包信息
    jobject packageInfo=env->CallObjectMethod(pm,mid,packageName,0x40); //GET_SIGNATURE=64;
    //获取packageInfo类
    cls=env->GetObjectClass(packageInfo);
    //获取签名数组属性的ID
    jfieldID fid=env->GetFieldID(cls,"signatures", "[Landroid/content/pm/Signature;");
    //得到签名数组
    jobjectArray  signatures=(jobjectArray)env->GetObjectField(packageName,fid);
    //得到签名
    jobject signature=env->GetObjectArrayElement(signatures,0);
    //获得Signature类
    cls=env->GetObjectClass(signature);
    mid = env->GetMethodID(cls, "toByteArray", "()[B");

    //当前应用签名信息
    jbyteArray signatureByteArray=(jbyteArray)env->CallObjectMethod(signature,mid);
    //转化为jstring
    jstring str=ToMD5(env,signatureByteArray);
    char *c_msg=(char *)env->GetStringUTFChars(str,0);

    if (strcmp(c_msg, RELEASE_SIGN_MD5) == 0) {
        return (env)->NewStringUTF(AUTH_KEY);
    } else {
        return (env)->NewStringUTF(c_msg);
    }
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}