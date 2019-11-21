package demo.lucius.androidproject.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import demo.lucius.androidproject.R;
import demo.lucius.androidproject.databinding.ActivityMainBinding;
import demo.lucius.androidproject.ui.fragment.DetailFragment;

public class MainActivity extends AppCompatActivity {

    //用于DataBinding表示
    ActivityMainBinding activityMainBinding;


    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        replaceFragment(new DetailFragment());
        initView();
    }

    //用于初始化页面
    private void initView() {
        activityMainBinding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
//                    case R.id.navigation_home:
//                        replaceFragment(new DetailFragment());
//                        Log.i("+++", "+++123");
//                        break;
//                    case R.id.navigation_dashboard:
//                        replaceFragment(new FragmentA());
//                        Log.i("+++", "+++1234");
//                        break;
//                    case R.id.navigation_notifications:
//                        replaceFragment(new FragmentD());
//                        Log.i("+++", "+++1235");
//                        break;
                    default:
                        break;

                }
                return true;
            }
        });

    }

    //用于切换Fragment
    private void replaceFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.homeContent, fragment).commit();

    }

}
