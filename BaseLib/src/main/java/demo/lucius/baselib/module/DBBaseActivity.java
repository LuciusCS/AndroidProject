package demo.lucius.baselib.module;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;

import demo.lucius.baselib.R;
import demo.lucius.baselib.databinding.ActivityDbBaseBinding;


public abstract class DBBaseActivity<T extends ViewDataBinding, V extends AndroidViewModel> extends AppCompatActivity {
    protected T dataBinding;
    protected V viewModel;

    //用于基类的布局文件
    private ActivityDbBaseBinding baseDatabinding;

    //用于表示ActionBar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_db_base);
    }

    //用于设置ActionBar
    protected void setActionBar(boolean back, String title) {
        setSupportActionBar(baseDatabinding.toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        //决定左上角是否有返回箭头，true表示有箭头并且可以点击
        actionBar.setDisplayHomeAsUpEnabled(back);
        baseDatabinding.toolbarTitle.setText(title);
    }
    //将ActionBar进行隐藏
    protected void hideActionBar(){
       baseDatabinding.toolbar.setVisibility(View.GONE);
    }

    /**
     * 用于设置布局文件中的Variable
     *
     * @return
     */
    protected  abstract int getBindingVariable();

    /**
     * 用于初始化View
     */
     protected abstract void initView();

    /**
     * 用于初始化ViewModel，如果有的话
     * @return
     */
    protected  abstract V getViewModel();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            //点击返回图标事件
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 用于添加布局文件
     * @param layout
     * @return
     */
    protected T putContentView(@LayoutRes int layout) {
        dataBinding= DataBindingUtil.inflate(getLayoutInflater(), layout, baseDatabinding.layoutContainer, true);
         this.viewModel=viewModel==null?getViewModel():viewModel;
         dataBinding.setVariable(getBindingVariable(),viewModel);

        return dataBinding;
    }
}
