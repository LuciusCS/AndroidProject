package demo.lucius.login;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import demo.lucius.login.databinding.ActivityLoginBinding;

public class LoginActivity extends DaggerAppCompatActivity {

    ActivityLoginBinding loginBinding;

    @Inject
    LoginNavigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation.finishLogin(LoginActivity.this);
            }
        });
    }
}
