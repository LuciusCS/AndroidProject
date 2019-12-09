package demo.lucius.kbaselib.common;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import demo.lucius.kbaselib.R;
import demo.lucius.kbaselib.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding activityTestBinding;

    //用于表示需要进行显示的数据
    private List<String>showList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTestBinding = DataBindingUtil.setContentView(this, R.layout.activity_test);

        for (int i=0;i<10;i++){
            showList.add(i+"+++++++");

//            new Thread(()->System.out.println("+++00")).start();
        }
//        TestAdapter testAdapter = new TestAdapter(showList);

//        Button button=(Button)findViewById(R.id.button1);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("info","点击");
//            }
//        });
//        button.setOnClickListener((e)->{
//            Log.i("info","点击");
//        });
//
//        Map<String,String>map=new HashMap<>();


    }
}
