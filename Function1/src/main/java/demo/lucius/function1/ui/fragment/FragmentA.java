package demo.lucius.function1.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;
import demo.lucius.function1.R;
import demo.lucius.function1.databinding.FragmentABinding;
import demo.lucius.function1.ui.activity.ActivityB;
import demo.lucius.function1.ui.navigation.Function1Navigation;

import javax.inject.Inject;

public class FragmentA extends DaggerFragment {


    //用于表示Databinding
    private FragmentABinding dataBinding;

    //用于表示根视图
    private View rootView;

    public FragmentA() {
    }

    @Inject
    public Function1Navigation function1Navigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        dataBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_a,container,false);
        rootView=dataBinding.getRoot();

        dataBinding.executePendingBindings();
        initView();
//        return super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }


    //用于初始化界面
    private void initView(){
        dataBinding.goToB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityB.getIntent(requireContext()));
            }
        });

        dataBinding.goToF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function1Navigation.goToF(requireActivity());
            }
        });

    }
}
