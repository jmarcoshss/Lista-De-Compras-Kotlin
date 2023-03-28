package com.example.listadecompras.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.listadecompras.R
import com.example.listadecompras.ui.adapter.ListaDeProdutosAdapter
import com.example.listadecompras.data.LdcDataBase
import com.example.listadecompras.databinding.ActivityListaDeProdutosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaDeProdutosActivity : AppCompatActivity() {

    private val produtoDao by lazy {
        val db = LdcDataBase.instancia(this)
        db.produtoDao()
    }
    private val adapter = ListaDeProdutosAdapter(context = this)
    private val binding by lazy { ActivityListaDeProdutosBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFAB()
    }

    override fun onResume() {
        lifecycleScope.launch {
            val produtos = withContext(Dispatchers.IO) {
                produtoDao.buscaTodos()
            }
            adapter.atualiza(produtos)
        }
        super.onResume()
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
                val produtos = withContext(Dispatchers.IO) {
                    produtoDao.remover(it)
                    produtoDao.buscaTodos()
                }
                adapter.atualiza(produtos)
            }

        }
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