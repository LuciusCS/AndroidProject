package demo.lucius.kbaselib.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import demo.lucius.kbaselib.R
import demo.lucius.kbaselib.databinding.ActivityConnectBinding;

class ConnectActivity : AppCompatActivity() {

    //用于表示Databinding
    var databing: ActivityConnectBinding? = null;

    var bundle: Bundle? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databing = DataBindingUtil.setContentView<ActivityConnectBinding>(this, R.layout.activity_connect);
        initView();

        bundle = this.intent.extras;
        if (bundle?.get("type").toString().equals("1")) {
             replaceFragment(WifiConnectFragmentTest())
        }else{
            replaceFragment( BleConnectFragmentTest())
        }


    }


    //用于初始化布局
    private fun initView() {

        databing?.connectBn?.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            replaceFragment(when (item.itemId) {
                R.id.navigation_ble_connect -> BleConnectFragmentTest()
                R.id.navigation_wifi_connect -> WifiConnectFragmentTest()
                else -> throw UnsupportedOperationException("Unknown tab!")
            })
            true
        })


    }

    //用于切换Fragment
    private fun replaceFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply { replace(R.id.content_fl, fragment) }.commitAllowingStateLoss();


}