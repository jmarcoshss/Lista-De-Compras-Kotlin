package com.example.listadecompras.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Entity
@Parcelize
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nome: String,
    val quantidade: BigDecimal,
    var unidade: String,
    val listaId: Long? = 0L,
    var valor: BigDecimal
):Parcelable