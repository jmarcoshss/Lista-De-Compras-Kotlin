package com.example.listadecompras.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.listadecompras.R
import com.example.listadecompras.data.LdcDataBase
import com.example.listadecompras.databinding.ActivityListaDeProdutosBinding
import com.example.listadecompras.extensions.formataParaMoedaBrasileira
import com.example.listadecompras.model.Produto
import com.example.listadecompras.ui.adapter.ListaDeProdutosAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class ListaDeProdutosActivity : ListaBaseActivity() {

    private val produtoDao by lazy {
        LdcDataBase.instancia(this).produtoDao()
    }

    private val adapter = ListaDeProdutosAdapter(context = this)
    private val binding by lazy { ActivityListaDeProdutosBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFAB()
        lifecycleScope.launch {
            launch {
                lista.filterNotNull().collect {
                    buscaProdutosDaLista(it.listaId)

                }
            }
        }
    }


    fun configuraRecyclerView() {
        val recyclerView = binding.activityListaDeProdutosRecyclerview
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        adapter.quandoClicaEmEditar = {
            val intent = Intent(this, FormularioProdutoActivity::class.java).apply {
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)
        }
        adapter.quandoClicaEmRemover = {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) { produtoDao.remover(it) }
                lista.firstOrNull()?.let {
                    buscaProdutosDaLista(it.listaId)
                }
            }
        }
    }

    suspend fun buscaProdutosDaLista(listaId: Long) {
        produtoDao.buscaTodosProdutosDaLista(listaId).collect { produto ->
            adapter.atualiza(produto)
            exibeValorTotal(produto)
        }
    }

    private fun exibeValorTotal(produto: List<Produto>) {
        val campoValorTotal = binding.activityListaDeProdutosValorTotal
        var resultado = BigDecimal(0)
        produto.forEach {
                val multiplicacao = it.valor.times(it.quantidade)
                resultado = resultado.plus(multiplicacao)
        }
        campoValorTotal.text = resultado.formataParaMoedaBrasileira()
    }

    fun configuraFAB() {
        val fab = binding.activityListaDeProdutosFab
        fab.setOnClickListener {
            vaiParaFormulario()
        }
    }

    fun vaiParaFormulario() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lista_de_produtos_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.lista_de_produtos_menu_ajuda -> {
                mostra()
            }
            R.id.lista_de_produtos_menu_sair -> {
                val intent = Intent(this, ListaDeListaActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun mostra() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ajuda")
        builder.setMessage(
            """
Para marcar, clique e segure o item da lista.
Para editar ou remover, clique no item da lista e aparecerá o menu """.trimMargin()
        )
        builder.setPositiveButton("Entendi") { dialog, which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}


