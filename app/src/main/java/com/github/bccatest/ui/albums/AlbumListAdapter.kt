package com.github.bccatest.ui.albums

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.bccatest.R
import com.github.bccatest.data.model.AlbumEntity

class AlbumListAdapter: RecyclerView.Adapter<AlbumListAdapter.AlbumListViewHolder>() {

    lateinit var mContext: Context
    lateinit var itemView: View
    private var items:List<AlbumEntity> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumListAdapter.AlbumListViewHolder {
        mContext = parent.context
        itemView = LayoutInflater.from(mContext).inflate(
            R.layout.item_album,
            parent,
            false
        )
        return AlbumListViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: AlbumListAdapter.AlbumListViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(): List<AlbumEntity>{
        return items
    }

    fun setItems(items: List<AlbumEntity>){
        this.items = items
        notifyDataSetChanged()
    }

    inner class AlbumListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(item: AlbumEntity){
            val itemContent = itemView.findViewById<TextView>(R.id.content)
            val itemTime = itemView.findViewById<TextView>(R.id.time)
            itemContent.text = item.title
            itemTime.text = item.cover
        }
    }
}
