package demo.lucius.funtion2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import demo.lucius.funtion2.R;
import demo.lucius.funtion2.databinding.FragmentDBinding;
import demo.lucius.funtion2.ui.activity.ActivityE;
import demo.lucius.funtion2.ui.navigation.Function2Navigation;

public class FragmentD extends DaggerFragment {


    //用于表示Databinding
    private FragmentDBinding dataBinding;

    //用于表示根视图
    private View rootView;

    public FragmentD() {
    }

//    @Inject
//    Function2Navigation function2Navigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        dataBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_d,container,false);
        rootView=dataBinding.getRoot();

        dataBinding.executePendingBindings();

//        return super.onCreateView(inflater, container, savedInstanceState);
        dataBinding.goToE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(ActivityE.getIntent(requireContext()));
            }
        });

        return rootView;
    }
}
