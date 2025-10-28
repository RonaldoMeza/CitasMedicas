package com.citasmedicas.util

import android.content.Context

object AppContextProvider {
    lateinit var context: Context
        private set

    fun init(ctx: Context) {
        context = ctx.applicationContext
    }
}
