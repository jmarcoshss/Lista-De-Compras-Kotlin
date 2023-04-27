package com.example.listadecompras.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.listadecompras.data.LdcDataBase
import com.example.listadecompras.databinding.ActivityListaDeListaBinding
import com.example.listadecompras.model.Lista
import com.example.listadecompras.preferences.dataStore
import com.example.listadecompras.preferences.listaSelecionadaPreferences
import com.example.listadecompras.ui.adapter.ListaDeListaAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaDeListaActivity : AppCompatActivity() {

    private val binding by lazy { ActivityListaDeListaBinding.inflate(layoutInflater) }
    private val adapter = ListaDeListaAdapter(context = this)
    private val listaDao by lazy {
        LdcDataBase.instancia(this).listaDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        botaoAdicionar()
        lifecycleScope.launch {
            listaDao.buscaLista().collect { lista ->
                adapter.atualiza(lista)
            }
        }
    }

    fun botaoAdicionar() {
        val botao = binding.activityListaDeListaBotao
        botao.setOnClickListener {
            val intent = Intent(this, FormularioListaActivity::class.java)
            startActivity(intent)
        }
    }

    fun configuraRecyclerView() {
        val recyclerView = binding.activityListaDeListaRecyclerview
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItem = {
            vaiParaListaDeProdutos(it)
        }
        adapter.quandoClicaBotaoRemover = {
            val nome = it.nome
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Deseja excluir $nome")
            builder.setPositiveButton("Sim") { dialog, which ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        listaDao.removeLista(it)
                        listaDao.removeTudo(it.listaId)
                    }
                    listaDao.buscaLista().collect { lista ->
                        adapter.atualiza(lista)
                    }
                }
            }
            builder.setNegativeButton("Não") { dialog, which -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        adapter.quandoClicaBotaoEditar = {
            Intent(this, FormularioListaActivity::class.java).apply {
                putExtra(CHAVE_LISTA_ID, it.listaId)
                startActivity(this)
            }
        }
    }

    private fun vaiParaListaDeProdutos(it: Lista) {
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[listaSelecionadaPreferences] = it.listaId
            }
            val intent = Intent(this@ListaDeListaActivity, ListaDeProdutosActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}