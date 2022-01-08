package com.github.bccatest.utils

import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.bccatest.AlbumApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Util {
    companion object {
        fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
            }
        }
    }
}
