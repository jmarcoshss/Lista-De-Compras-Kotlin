package com.example.listadecompras.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.listadecompras.data.LdcDataBase
import com.example.listadecompras.model.Lista
import com.example.listadecompras.model.Produto
import com.example.listadecompras.preferences.dataStore
import com.example.listadecompras.preferences.listaSelecionadaPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


abstract class ListaBaseActivity : AppCompatActivity() {


    private val listaDao by lazy {
        LdcDataBase.instancia(this).listaDao()
    }
    private val _lista: MutableStateFlow<Lista?> = MutableStateFlow(null)
    protected val lista: StateFlow<Lista?> = _lista

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            verificaListaSelecionada()
        }
    }

    private suspend fun verificaListaSelecionada() {
        dataStore.data.collect { preferences ->
            preferences[listaSelecionadaPreferences]?.let { listaId ->
                buscaLista(listaId)
            }
        }
    }

    private suspend fun buscaLista(listaId: Long): Lista? {
        return listaDao.buscaPorId(listaId).firstOrNull().also {
            _lista.value = it
            title = "Lista :${lista.value?.nome}"
        }
    }


}