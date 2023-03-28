package com.example.listadecompras.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.listadecompras.data.converters.BigDecimalCoverter
import com.example.listadecompras.data.dao.ProdutoDao
import com.example.listadecompras.model.Produto

@Database(entities = [Produto::class], version = 1, exportSchema = false)
@TypeConverters(BigDecimalCoverter::class)
abstract class LdcDataBase: RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    companion object {
        fun instancia(context: Context): LdcDataBase {
            return Room.databaseBuilder(
                context,
                LdcDataBase::class.java,
                "LDC.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}