package com.example.listadecompras.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras.data.LdcDataBase
import com.example.listadecompras.databinding.ListaCardBinding
import com.example.listadecompras.model.Lista


class ListaDeListaAdapter(
    listas:List<Lista> = emptyList(),
    private val context: Context,
    var quandoClicaNoItem:(lista:Lista)->Unit = {},
    var quandoClicaBotaoRemover:(lista:Lista) -> Unit = {},
    var quandoClicaBotaoEditar:(lista:Lista) -> Unit = {}
) : RecyclerView.Adapter<ListaDeListaAdapter.ViewHolder>() {

    private val listas = listas.toMutableList()

    inner class ViewHolder(private val binding: ListaCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var lista: Lista

        init {
            itemView.setOnClickListener {
                if (::lista.isInitialized){
                    quandoClicaNoItem(lista)
                }
            }
            binding.listaCardBotaoRemover.setOnClickListener {
                if (::lista.isInitialized) {
                    quandoClicaBotaoRemover(lista)
                }
            }
            binding.listaCardBotaoEditar.setOnClickListener {
                if (::lista.isInitialized) {
                    quandoClicaBotaoEditar(lista)
                }
            }
        }

        fun vincula(lista: Lista) {

            this.lista = lista
            val campoNome = binding.listaCardNome
            campoNome.text = lista.nome
            val campoDetalhes = binding.listaCardDescricao
            campoDetalhes.text = lista.detalhes

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ListaCardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lista = listas[position]
        holder.vincula(lista)
    }

    override fun getItemCount(): Int {
        return listas.size
    }

    fun atualiza(listas: List<Lista>) {
        this.listas.clear()
        this.listas.addAll(listas)
        notifyDataSetChanged()
    }

}