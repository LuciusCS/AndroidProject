package demo.lucius.kbaselib.common

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import demo.lucius.kbaselib.R
import demo.lucius.kbaselib.databinding.ActivityTestBinding

class TestAdapterActivity : AppCompatActivity() {

    //用于表示布局Databinding
//    var binding: ActivityTestBinding? = null
    var binding by autoCleared<ActivityTestBinding>()

    var list: MutableList<String> = arrayListOf()
//        var list  by autoCleared<List<String>>()
    var testAdapter by autoCleared<TestAdapter>()

    var info by autoCleared<String>()

//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        var  binding = DataBindingUtil.setContentView<ActivityTestBinding>(this, R.layout.activity_test)
////        binding.executePendingBindings()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityTestBinding>(this, R.layout.activity_test)
        binding.setLifecycleOwner(this)
        initView()

    }

    //用于初始化View
    fun initView() {
        var i = 0
        while (i < 10) {
            i++;
            list.add(i.toString())
//            LogUtilsKt.e("++", i.toString())
        }

        testAdapter = TestAdapter() { it ->
            LogUtilsKt.e(msg = i.toString());
        }

        binding?.stringListRv?.adapter = testAdapter
        testAdapter.submitList(list)
        binding?.stringListRv?.adapter?.notifyDataSetChanged()

    }

}