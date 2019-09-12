package demo.lucius.androidproject.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import demo.lucius.androidproject.R;
import demo.lucius.androidproject.databinding.FragmentMainBinding;

public class FunctionFragment extends Fragment {

    //用于表示Databinding
    FragmentMainBinding fragmentMainBinding;

    //用于表示根视图
    private View rootView;

    public FunctionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        rootView = fragmentMainBinding.getRoot();
        fragmentMainBinding.executePendingBindings();
        Log.i("++", "+++++++++++++");
        return rootView;

    }
}
