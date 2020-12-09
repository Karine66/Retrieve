package com.karine.retrieve.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

companion object {
    fun dateStringToLong(dateString: String): Long? {
        val sdf = SimpleDateFormat("dd/MM/yyy", Locale.FRANCE)
        try {
            val date: Date? = sdf.parse(dateString)
            return Objects.requireNonNull(date)?.time
        } catch (e: ParseException) {
            e.message
        }
        return 0
    }

    fun longDateToString(dateLong: Long): String? {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        return simpleDateFormat.format(dateLong)
    }
}
}