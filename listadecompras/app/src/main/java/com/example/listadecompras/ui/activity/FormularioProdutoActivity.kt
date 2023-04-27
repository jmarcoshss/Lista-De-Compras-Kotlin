package com.example.listadecompras.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.listadecompras.data.LdcDataBase
import com.example.listadecompras.databinding.ActivityFormularioProdutoBinding
import com.example.listadecompras.model.Lista
import com.example.listadecompras.model.Produto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class FormularioProdutoActivity : ListaBaseActivity() {

    private val binding by lazy { ActivityFormularioProdutoBinding.inflate(layoutInflater) }
    private val produtoDao by lazy {
        LdcDataBase.instancia(this).produtoDao()
    }
    private var produtoId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoSalvar()
        title = "Cadastrar Novo Produto"
        editar()

        lifecycleScope.launch {
            lista.filterNotNull().collect {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        tentaCarregar()
    }

    private fun tentaCarregar() {
        lifecycleScope.launch {
            val produto = withContext(Dispatchers.IO) {
                produtoDao.buscaTodosID(produtoId)
            }
            produto?.let {
                title = "Editar Produto"
                preencheCampos(it)
            }
        }
    }

    private fun editar() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    private fun preencheCampos(produtoCarregado: Produto) {
        binding.activityFormularioProdutoNome.setText(produtoCarregado.nome)
        binding.activityFormularioProdutoQuantidade.setText(produtoCarregado.quantidade.toPlainString())
        binding.activityFormularioProdutoUnidade.setText(produtoCarregado.unidade)
        binding.activityFormularioProdutoValor.setText(produtoCarregado.valor.toPlainString())
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoSalvar
        botaoSalvar.setOnClickListener {

            lifecycleScope.launch {
                lista.value?.let {
                    val produtoNovo = criaProduto(it.listaId)
                    withContext(Dispatchers.IO) {
                        produtoDao.salva(produtoNovo)
                    }
                    finish()
                }

            }
        }
    }

   private fun criaProduto(listaId:Long): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()
        val campoQuantidade = binding.activityFormularioProdutoQuantidade
        val quantidadeEmTexto = campoQuantidade.text.toString()
        val quantidade = if (quantidadeEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(quantidadeEmTexto)
        }
        val campoUnidade = binding.activityFormularioProdutoUnidade
        val unidade = campoUnidade.text.toString()
       val campoValor = binding.activityFormularioProdutoValor
       val valorEmTexto = campoValor.text.toString()
       val valor = if (valorEmTexto.isBlank()) {
           BigDecimal.ZERO
       } else {
           BigDecimal(valorEmTexto)
       }



        return Produto(
            id = produtoId,
            nome = nome,
            quantidade = quantidade,
            unidade = unidade,
            listaId = listaId,
            valor = valor
        )

    }


}