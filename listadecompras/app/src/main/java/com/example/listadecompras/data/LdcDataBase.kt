package com.example.listadecompras.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.listadecompras.data.converters.BigDecimalCoverter
import com.example.listadecompras.data.dao.ListaDao
import com.example.listadecompras.data.dao.ProdutoDao
import com.example.listadecompras.model.Lista
import com.example.listadecompras.model.Produto

@Database(
    entities = [Produto::class, Lista::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 2, to = 3)],
    exportSchema = true
)
@TypeConverters(BigDecimalCoverter::class)
abstract class LdcDataBase: RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun listaDao():ListaDao

    companion object {
        @Volatile
        private var db:LdcDataBase? = null
        fun instancia(context: Context): LdcDataBase {
            return db?: Room.databaseBuilder(
                context,
                LdcDataBase::class.java,
                "LDC.db"
            ).build().also {
                    db = it
                }
        }
    }
}