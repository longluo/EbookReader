package com.longluo.ebookreader.ui.book.source.manage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.longluo.ebookreader.R
import com.longluo.ebookreader.base.BaseDialogFragment
import com.longluo.ebookreader.base.adapter.ItemViewHolder
import com.longluo.ebookreader.base.adapter.RecyclerAdapter
import com.longluo.ebookreader.constant.AppPattern
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.databinding.DialogEditTextBinding
import com.longluo.ebookreader.databinding.DialogRecyclerViewBinding
import com.longluo.ebookreader.databinding.ItemGroupManageBinding
import com.longluo.ebookreader.lib.dialogs.alert
import com.longluo.ebookreader.lib.theme.backgroundColor
import com.longluo.ebookreader.lib.theme.primaryColor
import com.longluo.ebookreader.ui.widget.recycler.VerticalDivider
import com.longluo.ebookreader.utils.applyTint
import com.longluo.ebookreader.utils.getSize
import com.longluo.ebookreader.utils.requestInputMethod
import com.longluo.ebookreader.utils.splitNotBlank
import com.longluo.ebookreader.utils.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class GroupManageDialog : BaseDialogFragment(), Toolbar.OnMenuItemClickListener {
    private val viewModel: BookSourceViewModel by activityViewModels()
    private lateinit var adapter: GroupAdapter
    private val binding by viewBinding(DialogRecyclerViewBinding::bind)

    override fun onStart() {
        super.onStart()
        val dm = requireActivity().getSize()
        dialog?.window?.setLayout((dm.widthPixels * 0.9).toInt(), (dm.heightPixels * 0.9).toInt())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_recycler_view, container)
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        view.setBackgroundColor(backgroundColor)
        binding.toolBar.setBackgroundColor(primaryColor)
        binding.toolBar.title = getString(R.string.group_manage)
        binding.toolBar.inflateMenu(R.menu.group_manage)
        binding.toolBar.menu.applyTint(requireContext())
        binding.toolBar.setOnMenuItemClickListener(this)
        adapter = GroupAdapter(requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(VerticalDivider(requireContext()))
        binding.recyclerView.adapter = adapter
        initData()
    }

    private fun initData() {
        launch {
            appDb.bookSourceDao.flowGroup().collect {
                val groups = linkedSetOf<String>()
                it.map { group ->
                    groups.addAll(group.splitNotBlank(AppPattern.splitGroupRegex))
                }
                adapter.setItems(groups.toList())
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_add -> addGroup()
        }
        return true
    }

    @SuppressLint("InflateParams")
    private fun addGroup() {
        alert(title = getString(R.string.add_group)) {
            val alertBinding = DialogEditTextBinding.inflate(layoutInflater)
            alertBinding.editView.setHint(R.string.group_name)
            customView { alertBinding.root }
            yesButton {
                alertBinding.editView.text?.toString()?.let {
                    if (it.isNotBlank()) {
                        viewModel.addGroup(it)
                    }
                }
            }
            noButton()
        }.show().requestInputMethod()
    }

    @SuppressLint("InflateParams")
    private fun editGroup(group: String) {
        alert(title = getString(R.string.group_edit)) {
            val alertBinding = DialogEditTextBinding.inflate(layoutInflater)
            alertBinding.editView.setHint(R.string.group_name)
            alertBinding.editView.setText(group)
            customView { alertBinding.root }
            yesButton {
                viewModel.upGroup(group, alertBinding.editView.text?.toString())
            }
            noButton()
        }.show().requestInputMethod()
    }

    private inner class GroupAdapter(context: Context) :
        RecyclerAdapter<String, ItemGroupManageBinding>(context) {

        override fun getViewBinding(parent: ViewGroup): ItemGroupManageBinding {
            return ItemGroupManageBinding.inflate(inflater, parent, false)
        }

        override fun convert(
            holder: ItemViewHolder,
            binding: ItemGroupManageBinding,
            item: String,
            payloads: MutableList<Any>
        ) {
            binding.run {
                root.setBackgroundColor(context.backgroundColor)
                tvGroup.text = item
            }
        }

        override fun registerListener(holder: ItemViewHolder, binding: ItemGroupManageBinding) {
            binding.apply {
                tvEdit.setOnClickListener {
                    getItem(holder.layoutPosition)?.let {
                        editGroup(it)
                    }
                }
                tvDel.setOnClickListener {
                    getItem(holder.layoutPosition)?.let { viewModel.delGroup(it) }
                }
            }
        }
    }

}