package demo.lucius.function1app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dagger.android.support.DaggerAppCompatActivity;
import demo.lucius.function1app.R;

public class ActivityA extends DaggerAppCompatActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, ActivityA.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
    }
}
