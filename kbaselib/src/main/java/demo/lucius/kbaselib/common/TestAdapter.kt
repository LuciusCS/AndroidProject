package demo.lucius.kbaselib.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.android.example.github.ui.common.DataBoundListAdapter
import demo.lucius.kbaselib.R
import demo.lucius.kbaselib.databinding.ItemInfoBinding

class TestAdapter(
                  private val clickCallback: ((String) -> Unit?)
) : DataBoundListAdapter<String, ItemInfoBinding>(diffCallback = object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
                && oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
                && oldItem == newItem
    }
}) {
    override fun createBinding(parent: ViewGroup): ItemInfoBinding {
        val binding = DataBindingUtil.inflate<ItemInfoBinding>(LayoutInflater.from(parent.context),
                R.layout.item_info, parent, false)
        binding.root.setOnClickListener {
            binding.test?.let {
                clickCallback?.invoke(it)
            }
        }
        return binding;
    }

    override fun bind(binding: ItemInfoBinding, item: String) {
        binding.test = item
    }
}