package com.example.listadecompras.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.listadecompras.model.Lista
import com.example.listadecompras.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ListaDao {

    @Query("SELECT * FROM Lista")
    fun buscaLista():Flow<List<Lista>>

    @Query("SELECT * FROM Lista WHERE listaId =:listaId")
    fun buscaListaId(listaId:Long):Lista?

    @Query("SELECT * FROM Lista WHERE listaId =:idLista")
    fun buscaPorId(idLista: Long):Flow<Lista>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salvaLista(vararg lista: Lista)

    @Delete
    fun removeLista(lista: Lista)

    @Query("DELETE FROM Produto WHERE listaId =:idDaLista")
    fun removeTudo(idDaLista: Long)
}