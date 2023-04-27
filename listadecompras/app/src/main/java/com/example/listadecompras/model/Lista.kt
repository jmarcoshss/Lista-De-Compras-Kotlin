package com.example.listadecompras.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Lista(
    @PrimaryKey(autoGenerate = true)
    val listaId:Long = 0L,
    val nome:String,
    val detalhes:String,
):Parcelable
