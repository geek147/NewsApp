package com.envious.kumparan.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.envious.domain.model.Album
import com.envious.kumparan.R
import com.envious.kumparan.databinding.ListRowAlbumBinding

class AlbumAdapter(private var fragment: Fragment) : RecyclerView.Adapter<AlbumAdapter.MainViewHolder>() {
    private var listItem: MutableList<Album> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding: ListRowAlbumBinding = DataBindingUtil.inflate(LayoutInflater.from(fragment.context), R.layout.list_row_album, parent, false)
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
        val item: Album = listItem[position]
        return item.id.toLong()
    }

    fun addData(list: List<Album>) {
        this.listItem.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(list: List<Album>) {
        this.listItem.clear()
        this.listItem.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listItem[holder.bindingAdapterPosition].let {
            holder.bindData(it, fragment)
        }
    }

    class MainViewHolder(private val binding: ListRowAlbumBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: Album, fragment: Fragment) {
            binding.album = model
            if (model.photos.isEmpty()) {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView(
                            title = resources.getString(R.string.empty_state_title),
                            message = resources.getString(R.string.empty_state_message)
                        )
                    }

                    recyclerview.visibility = View.GONE
                }
            } else {
                with(binding) {
                    recyclerview.setHasFixedSize(true)
                    val gridLayoutManager = GridLayoutManager(fragment.context, 2)
                    recyclerview.layoutManager = gridLayoutManager
                    recyclerview.itemAnimator = null
                    val adapter = PhotoAdapter(fragment)
                    adapter.setHasStableIds(true)
                    recyclerview.adapter = adapter
                    adapter.setData(model.photos)
                    recyclerview.visibility = View.VISIBLE
                    errorView.visibility = View.GONE
                }
            }
        }
    }
}
