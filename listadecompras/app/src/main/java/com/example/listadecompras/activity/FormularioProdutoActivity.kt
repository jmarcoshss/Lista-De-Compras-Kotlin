package com.example.listadecompras.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.listadecompras.dao.ProdutoDAO
import com.example.listadecompras.databinding.ActivityFormularioProdutoBinding
import com.example.listadecompras.model.Produto

class FormularioProdutoActivity: AppCompatActivity() {
    private val binding by lazy { ActivityFormularioProdutoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoSalvar()
    }

    private fun configuraBotaoSalvar() {
        val dao = ProdutoDAO()
        val botaoSalvar = binding.activityFormularioProdutoButton
        botaoSalvar.setOnClickListener {
            val produtoNovo = criaProduto()
            dao.adicionaProduto(produtoNovo)
            finish()
        }

    }
    private fun criaProduto():Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()
        val campoQuantidade = binding.activityFormularioProdutoQuantidade
        val quantidade = campoQuantidade.text.toString()


        return Produto(nome = nome,
            quantidade = quantidade)

    }
}