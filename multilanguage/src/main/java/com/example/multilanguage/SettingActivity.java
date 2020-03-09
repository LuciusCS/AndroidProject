package com.example.multilanguage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.language_rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.chinese_rb:
                        SPUtil.getInstance(SettingActivity.this).saveLanguage(1);
                        break;
                    case R.id.english_rb:
                        SPUtil.getInstance(SettingActivity.this).saveLanguage(3);

                        break;
                    default:
                        break;
                }

                //用于重启Activity
                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                SettingActivity.this.startActivity(intent);
            }
        });
    }
}
