package com.github.bccatest.ui.main

import android.graphics.*
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.bccatest.R
import com.github.bccatest.base.BaseActivity
import com.github.bccatest.databinding.ActivityMainBinding
import com.github.bccatest.viewmodel.AlbumViewModel
import com.github.bccatest.ui.albums.AlbumListAdapter
import com.github.bccatest.data.model.AlbumEntity
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.inputmethod.InputMethodManager
import com.github.bccatest.utils.Util.Companion.repeatOnStarted
import kotlinx.coroutines.flow.collect
import android.util.Log

class MainActivity : BaseActivity<ActivityMainBinding, AlbumViewModel>() {

    override val layoutResID: Int = R.layout.activity_main
    override val viewModel by viewModel<AlbumViewModel>()
    private var albumListAdapter: AlbumListAdapter? = null

    override fun initVariable() {
        binding.viewModel = viewModel
        binding.isLiveData = viewModel.isLiveData
    }

    override fun initView() {
        // recycler view
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        albumListAdapter = AlbumListAdapter()
        binding.apply {
            albumList.apply {
                setHasFixedSize(true)
                this.layoutManager = layoutManager
                adapter = albumListAdapter
            }
        }
    }

    /**
     * Set UI listeners
     */
    override fun initListener() {
        binding.apply {
            filterText.doOnTextChanged { text, _, _, _ ->
                viewModel?.filterAlbums(text.toString())
            }
            manual.setOnClickListener {
                viewModel?.insertAlbum(AlbumEntity(-1,0L,filter.editText?.text.toString(),"na","na"))
                filter.editText?.setText("")
                hideKeyboard()
            }
        }
    }

    /**
     * Hiding keyboard
     */
    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun initObserver() {
        repeatOnStarted {
            viewModel.albumListSeeable.collect {
                albumListAdapter?.setItems(it)
                binding.apply {
                   albumCount.setText(""+it.size)
                   binding.isLiveData = viewModel!!.isLive()
                }
            }
        }
    }

}
