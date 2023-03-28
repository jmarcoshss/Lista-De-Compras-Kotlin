package com.example.listadecompras.data.dao

import androidx.room.*
import com.example.listadecompras.model.Produto


@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos() : List<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(vararg produto: Produto)

    @Delete
    fun remover(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id=:id")
    fun buscaTodosID(id:Long):Produto?

    @Update
    fun edita(produto: Produto)

}