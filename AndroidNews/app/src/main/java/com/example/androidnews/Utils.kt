package com.example.androidnews
import android.content.Context
import java.io.IOException
import android.util.Log


fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    //Try block since the file may not exists
    try {
        //access assets manager in context
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

