package com.example.myapplication.util

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher

class DebounceTextWatcher(
    private val delayMillis: Long = 1500,
    private val onDebouncedTextChanged: (String) -> Unit
) : TextWatcher {

    private val handler = Handler(Looper.getMainLooper())
    private var lastRunnable: Runnable? = null

    override fun afterTextChanged(s: Editable?) {
        lastRunnable?.let { handler.removeCallbacks(it) }

        val text = s.toString()
        if (text.length < 3) return

        lastRunnable = Runnable {
            onDebouncedTextChanged(text)
        }

        handler.postDelayed(lastRunnable!!, delayMillis)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}