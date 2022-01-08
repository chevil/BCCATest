package com.github.bccatest.ui.albums

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.graphics.drawable.Drawable
import android.net.Uri
import java.net.URLEncoder
import androidx.recyclerview.widget.RecyclerView
import com.github.bccatest.R
import com.github.bccatest.data.model.AlbumEntity
import com.github.bccatest.AlbumApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import android.webkit.WebSettings 

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
        fun bind(item: AlbumEntity){
            val itemTitle = itemView.findViewById<TextView>(R.id.title)
            val itemId = itemView.findViewById<TextView>(R.id.albumid)
            val itemThumbnail : ImageView = itemView.findViewById<ImageView>(R.id.thumbnail) as ImageView
            itemTitle.text = item.title
            itemId.text = ""+item.id
            val glideUrl : GlideUrl = GlideUrl(item.thumbnail, LazyHeaders.Builder()
                    .addHeader("User-Agent", WebSettings.getDefaultUserAgent(AlbumApplication.applicationContext()))
                    .build());
            Log.v( "Album list", "loading : <${glideUrl}>" )
            Glide.with(AlbumApplication.applicationContext())
                 .load(glideUrl)
                 .error(R.drawable.app_icon)
                 .listener(object : RequestListener<Drawable> {
                               override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                               ): Boolean {
                                    Log.v( "Album list", "loading failed : ${e}" )
                                    return true
                               }

                               override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                               ): Boolean {
                                    return false
                               }
                 })
                 .into(itemThumbnail)
        }
    }
}
