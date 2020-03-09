package com.example.multilanguage;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

public class LanguageApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(MultiLanguage.setLocal(base));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MultiLanguage.setLocal(this);
    }
}
