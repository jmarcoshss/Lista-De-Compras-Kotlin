package com.example.listadecompras.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.listadecompras.data.LdcDataBase
import com.example.listadecompras.databinding.ActivityFormularioListaBinding
import com.example.listadecompras.model.Lista
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormularioListaActivity:AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioListaBinding.inflate(layoutInflater)
    }
    private val listaDao by lazy {
        LdcDataBase.instancia(this).listaDao()
    }
    private var idLista = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoSalvar()
        editar()
        title = "Adiconar lista"
    }

    override fun onResume() {
        super.onResume()
        tentaCarregar()
    }

    private fun tentaCarregar() {
        lifecycleScope.launch{
            val lista = withContext(Dispatchers.IO){
                listaDao.buscaListaId(idLista)
            }
            lista?.let {
                title = "Editar lista"
                preencheCampos(it)
            }
        }
    }

    private fun editar() {
        idLista = intent.getLongExtra(CHAVE_LISTA_ID, 0L)
    }

    private fun preencheCampos(listaCarregada:Lista) {
        binding.activityFormularioListaNome.setText(listaCarregada.nome)
        binding.activityFormularioListaDetalhes.setText(listaCarregada.detalhes)
    }


    fun configuraBotaoSalvar() {
        val botao  = binding.activityFormularioListaBotao
        botao.setOnClickListener {
            val listaNova = criaLista()
            lifecycleScope.launch {
                withContext(Dispatchers.IO){
                    listaDao.salvaLista(listaNova)
                }
                finish()
            }
        }
    }

    fun criaLista(): Lista {
        val campoNome = binding.activityFormularioListaNome
        val nome = campoNome.text.toString()
        val campoDetalhes = binding.activityFormularioListaDetalhes
        val detalhes = campoDetalhes.text.toString()

        return Lista (
            listaId = idLista,
            nome = nome,
            detalhes = detalhes
        )
    }


}