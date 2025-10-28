package com.citasmedicas.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    private val isoDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val uiDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun uiDateToIso(ui: String): String = try {
        val d = uiDate.parse(ui)
        if (d != null) isoDate.format(d) else ui
    } catch (_: Exception) { ui }

    fun isoToUiDate(iso: String): String = try {
        val d = isoDate.parse(iso)
        if (d != null) uiDate.format(d) else iso
    } catch (_: Exception) { iso }

    fun uiTimeTo24(time: String): String = time // asumimos HH:mm en UI
    fun time24ToUi(time24: String): String = time24

    fun todayIso(): String = isoDate.format(java.util.Date())
}
