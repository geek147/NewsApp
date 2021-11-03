package com.envious.kumparan.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.envious.domain.model.Comment
import com.envious.kumparan.R
import com.envious.kumparan.databinding.ListRowCommentBinding
import com.envious.kumparan.ui.fragments.DetailPostFragment

class CommentAdapter(private var fragment: Fragment) : RecyclerView.Adapter<CommentAdapter.MainViewHolder>() {
    private var listItem: MutableList<Comment> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding: ListRowCommentBinding = DataBindingUtil.inflate(LayoutInflater.from(fragment.context), R.layout.list_row_comment, parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (listItem.isNullOrEmpty()) {
            0
        } else {
            listItem.size
        }
    }

    override fun getItemId(position: Int): Long {
        val item: Comment = listItem[position]
        return item.id.toLong()
    }

    fun addData(list: List<Comment>) {
        this.listItem.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(list: List<Comment>) {
        this.listItem.clear()
        this.listItem.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listItem[holder.bindingAdapterPosition].let {
            holder.bindData(it, fragment)
        }
    }

    class MainViewHolder(private val binding: ListRowCommentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: Comment, fragment: Fragment) {
            val bundle = Bundle()
            bundle.putInt(DetailPostFragment.EXTRA_POST_ID, model.id)
            binding.comment = model
            itemView.setOnClickListener {
                fragment.findNavController().navigate(R.id.action_detailPostFragment_to_userDetailFragment, bundle)
            }
        }
    }
}
