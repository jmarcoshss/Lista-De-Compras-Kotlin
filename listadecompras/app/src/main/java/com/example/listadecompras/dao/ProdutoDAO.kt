package com.example.listadecompras.dao

import com.example.listadecompras.model.Produto

class ProdutoDAO {

    fun adicionaProduto(produto: Produto) {
       produtos.add(produto)
    }

    fun buscaProdutos(): List<Produto> {
        return produtos.toList()
    }

    companion object {
        private val produtos = mutableListOf<Produto>()
    }
}