package com.example.multilanguage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class SPUtil {

    private final String SP_NAME = "language_setting";
    private final String TAG_LANGUAGE = "language_select";
    private static volatile SPUtil instance;

    private final SharedPreferences mSharedPreferences;

    public SPUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }


    public void saveLanguage(int select) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt(TAG_LANGUAGE, select);
        edit.commit();
    }

    public int getSelectLanguage() {
        return mSharedPreferences.getInt(TAG_LANGUAGE, 0);
    }

    public static SPUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (SPUtil.class) {
                if (instance == null) {
                    instance = new SPUtil(context);
                }
            }
        }
        return instance;
    }

    /**
     * 用来保存系统语言
     */
    private Locale systemCurrentLocal = Locale.ENGLISH;


    public Locale getSystemCurrentLocal() {
        return systemCurrentLocal;
    }

    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    public static Locale getSystemLocale(Context context) {
        return SPUtil.getInstance(context).getSystemCurrentLocal();
    }




}
