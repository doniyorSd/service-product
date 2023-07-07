package com.example.sero_service_admin.utils

import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {
    private const val NAME = "sub"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var pref: SharedPreferences

    fun init(context: Context) {
        pref = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var id: String?
        get() = pref.getString("id", "no")
        set(value) = pref.edit {
            if (value != null)
                it.putString("id", value)
        }
}