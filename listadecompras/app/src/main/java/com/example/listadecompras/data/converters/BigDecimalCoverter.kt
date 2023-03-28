package com.example.listadecompras.data.converters

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalCoverter {
    @TypeConverter
    fun deDouble(quantidade:Double?): BigDecimal {
        return quantidade?.let { BigDecimal(quantidade.toString()) } ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun bigDecimalParaDouble(quantidade: BigDecimal?): Double?{
        return quantidade?.let { quantidade.toDouble() }
    }
}