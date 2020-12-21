package com.example.androidnews

import java.io.Serializable

data class Source(val titleSource: String, val content: String, var isChecked: Boolean) :
    Serializable
