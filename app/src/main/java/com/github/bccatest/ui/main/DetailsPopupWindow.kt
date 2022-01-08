/**
 * Created by chevil on 08/01/2022.
 * @author chevil@giss.tv
 */

package com.github.bccatest.ui.main
  
import android.graphics.*
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import com.github.bccatest.R
import com.github.bccatest.utils.Constants
import com.github.bccatest.data.model.AlbumEntity
import com.github.bccatest.AlbumApplication
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.util.Log
import android.animation.Animator 
import android.animation.ObjectAnimator 
import android.animation.AnimatorSet 
import android.graphics.drawable.Drawable 
import android.webkit.WebSettings
import com.xu.xpopupwindow.XPopupWindow

// Glide
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class AlbumPopupWindow : XPopupWindow {

    constructor(ctx: Context, album: AlbumEntity) : super(ctx) {
       Log.v( Constants.LOGTAG, "setting : <${album}>" )
       initViews()
       titleText.setText(album.title)
       val glideUrl : GlideUrl = GlideUrl(album.cover, LazyHeaders.Builder()
                 .addHeader("User-Agent", WebSettings.getDefaultUserAgent(AlbumApplication.applicationContext()))
                 .build());
       Log.v( Constants.LOGTAG, "loading : <${glideUrl}>" )
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
                            Log.v( Constants.LOGTAG, "loading failed : ${e}" )
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
                 .into(coverImage)
    }

    lateinit var coverImage : ImageView
    lateinit var titleText : TextView

    /**
     * Set the layoutId of popupwindow
     */
    override fun getLayoutId(): Int {
        return R.layout.details_album
    }

    /**
     * Set parentNodeId of layout
     */
    override fun getLayoutParentNodeId(): Int {
        return R.id.main
    }

    /**
     * Initialization interface
     */
    override fun initViews() {
        coverImage = findViewById<ImageView>(R.id.cover)!!
        coverImage.setOnClickListener { dismiss() }
        titleText = findViewById<TextView>(R.id.title)!!
    }

    /**
     * Initialization data
     */
    override fun initData() {
        // Set pop-up background transparency
        setShowingBackgroundAlpha(0.7f)
    }

    /**
     * Set pop-up animation for the pop-up window. If you don't want to set it or you want to set it through xml, set the return value to - 1
     */
    override fun startAnim(view: View): Animator? {
        var animatorX: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        var animatorY: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        var set = AnimatorSet()
        set.play(animatorX).with(animatorY)
        set.duration = 500
        return set
    }

    /**
     * Set exit animation for the pop-up window. If you don't want to set it or you want to set it through xml, set the return value to - 1
     */
    override fun exitAnim(view: View): Animator? {
        var animatorX: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
        var animatorY: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f)
        var set = AnimatorSet()
        set.play(animatorX).with(animatorY)
        set.duration = 700
        return set
    }

    /**
     * The animation is set by xml. The method of writing xml is the same as that of setting animation in native popupwindow
     */
    override fun animStyle(): Int {
        return -1
    }
}
