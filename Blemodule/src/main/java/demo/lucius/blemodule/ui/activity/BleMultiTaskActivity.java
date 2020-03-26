package demo.lucius.blemodule.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import demo.lucius.blemodule.base.BaseViewModel;
import demo.lucius.blemodule.base.DBBaseActivity;
import demo.lucius.blemodule.databinding.ActivityBleMultiTaskBinding;


/***
 * 用于对区域内的所有BLE进行自动操作
 */
public class BleMultiTaskActivity extends DBBaseActivity<ActivityBleMultiTaskBinding,BaseViewModel> {


    //用于表示绑定的布局



    @Override
    protected int getBindingVariable() {

        return 0;

    }

    @Override
    protected void initView() {
        setActionBar(true,"蓝牙诊断");

    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }
}
