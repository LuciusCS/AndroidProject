package demo.lucius.baselib.module;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import demo.lucius.baselib.viewmodel.BaseViewModel;


public abstract class DBBaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {

    //用于表示子类的Databinding
    private T databinding;

    private V viewModel;

    //用于表示根视图
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModel();

        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        databinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        rootView = databinding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databinding.setVariable(getBindingVariable(), viewModel);
        databinding.executePendingBindings();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 用于获取布局文件的id
     *
     * @return
     */
    protected abstract @LayoutRes
    int getLayoutId();

    /**
     * 用于设置ViewModel
     *
     * @return
     */
    protected abstract V getViewModel();

    /**
     * 用于获取databinding
     *
     * @return
     */
    protected T getViewDataBinding() {
        return databinding;
    }

    /**
     * 用于获取绑定布局文件的
     *
     * @return
     */
    protected abstract int getBindingVariable();
}
