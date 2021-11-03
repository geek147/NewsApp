package com.envious.kumparan.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.envious.domain.model.Photo
import com.envious.kumparan.R
import com.envious.kumparan.databinding.ListRowPhotoBinding
import com.envious.kumparan.ui.fragments.PhotoDetailFragment
import com.envious.kumparan.util.BindingConverters
import okhttp3.internal.userAgent

class PhotoAdapter(private var fragment: Fragment) : RecyclerView.Adapter<PhotoAdapter.MainViewHolder>() {
    private var listItem: MutableList<Photo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding: ListRowPhotoBinding = DataBindingUtil.inflate(LayoutInflater.from(fragment.context), R.layout.list_row_photo, parent, false)
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
        val item: Photo = listItem[position]
        return item.id.toLong()
    }

    fun addData(list: List<Photo>) {
        this.listItem.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(list: List<Photo>) {
        this.listItem.clear()
        this.listItem.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listItem[holder.bindingAdapterPosition].let {
            holder.bindData(it, fragment)
        }
    }

    class MainViewHolder(private val binding: ListRowPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: Photo, fragment: Fragment) {
            val url = GlideUrl(
                model.thumbnailUrl,
                LazyHeaders.Builder()
                    .addHeader("User-Agent", userAgent)
                    .build()
            )
            BindingConverters.loadImage(binding.ivMoviePoster, url.toStringUrl())

            val bundle = Bundle()
            bundle.putString(PhotoDetailFragment.EXTRA_TITLE, model.title)
            bundle.putString(PhotoDetailFragment.EXTRA_URL, model.url)
            binding.photo = model
            itemView.setOnClickListener {
                fragment.findNavController().navigate(R.id.action_userDetailFragment_to_photoDetailFragment, bundle)
            }
        }
    }
}
