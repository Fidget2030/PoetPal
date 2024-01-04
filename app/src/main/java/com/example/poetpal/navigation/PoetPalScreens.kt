package com.example.poetpal.navigation

import androidx.annotation.StringRes
import com.example.poetpal.R

enum class PoetPalScreens(
    @StringRes val title: Int,
) {
    Home(title = R.string.home),
    Write(title = R.string.write),
    Read(title = R.string.read),
    Dictionary(title = R.string.dict),
}
