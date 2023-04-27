package com.example.listadecompras.data.dao

import androidx.room.*
import com.example.listadecompras.model.Produto
import kotlinx.coroutines.flow.Flow


@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos() : Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE listaId =:listaId")
    fun buscaTodosProdutosDaLista(listaId:Long) : Flow<List<Produto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(vararg produto: Produto)

    @Delete
    fun remover(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id=:id")
    fun buscaTodosID(id:Long):Produto?

}