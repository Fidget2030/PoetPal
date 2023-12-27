package com.example.poetpal.ui

import androidx.annotation.StringRes
import com.example.poetpal.R

enum class PoetPalScreens(
    @StringRes val title: Int,
) {
    Welcome(title = R.string.welcome),
    Write(title = R.string.write),
    Read(title = R.string.read),
}
