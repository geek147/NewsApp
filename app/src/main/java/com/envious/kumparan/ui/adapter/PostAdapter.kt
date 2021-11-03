package com.envious.kumparan.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.envious.domain.model.Post
import com.envious.kumparan.R
import com.envious.kumparan.databinding.ListRowPostBinding
import com.envious.kumparan.ui.fragments.DetailPostFragment


class PostAdapter(private var fragment: Fragment) : RecyclerView.Adapter<PostAdapter.MainViewHolder>() {
    private var listItem: MutableList<Post> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding: ListRowPostBinding = DataBindingUtil.inflate(LayoutInflater.from(fragment.context), R.layout.list_row_post, parent, false)
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
        val item: Post = listItem[position]
        return item.id.toLong()
    }

    fun addData(list: List<Post>) {
        this.listItem.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(list: List<Post>) {
        this.listItem.clear()
        this.listItem.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listItem[holder.bindingAdapterPosition].let {
            holder.bindData(it, fragment)
        }
    }

    class MainViewHolder(private val binding: ListRowPostBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: Post, fragment: Fragment) {
            val bundle = Bundle()
            bundle.putInt(DetailPostFragment.EXTRA_POST_ID, model.userId)
            bundle.putString(DetailPostFragment.EXTRA_USER_NAME, model.userName)
            bundle.putString(DetailPostFragment.EXTRA_TITLE, model.title)
            bundle.putString(DetailPostFragment.EXTRA_BODY, model.body)
            binding.post = model
            itemView.setOnClickListener {
                fragment.findNavController().navigate(R.id.action_postFragment_to_detailPostFragment, bundle)
            }
        }
    }
}
