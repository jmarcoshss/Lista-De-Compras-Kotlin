package com.example.listadecompras.ui.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.listadecompras.R
import com.example.listadecompras.databinding.ProdutoCardBinding
import com.example.listadecompras.extensions.formataParaMoedaBrasileira
import com.example.listadecompras.model.Produto

class ListaDeProdutosAdapter(
    produtos: List<Produto> = emptyList(),
    private val context: Context,
    var quandoClicaEmEditar: (produto: Produto) -> Unit = {},
    var quandoClicaEmRemover: (produto: Produto) -> Unit = {}
) : Adapter<ListaDeProdutosAdapter.ViewHolder>() {

    private val produtos = produtos.toMutableList()

    inner class ViewHolder(private val binding: ProdutoCardBinding) :
        RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {

        private lateinit var produto: Produto

        init {
            itemView.setOnClickListener {
                if (::produto.isInitialized) {
                    popUpDoAdapter(it)
                }
            }
            itemView.setOnLongClickListener {
                val card = binding.card
                card.setChecked(!card.isChecked)
                true
            }
        }

        fun vinculador(produto: Produto) {

            this.produto = produto

            val nome = binding.produtoCardNome
            nome.text = produto.nome

            val quantidade = binding.produtoCardQuantidade
            quantidade.text = produto.quantidade.toString()

            val unidade = binding.produtoCardUnidade
            unidade.text = produto.unidade

            val valor = binding.produtoCardValor
            valor.text = produto.valor.formataParaMoedaBrasileira()

            binding.card.setOnCheckedChangeListener { card, isChecked ->
                salvaEstadoCheckbox(produto, isChecked)
            }
            binding.card.isChecked = recuperaEstadoCheckbox(produto)
        }

        fun popUpDoAdapter(v: View) {
            PopupMenu(context, v).apply {
                setOnMenuItemClickListener(this@ViewHolder)
                inflate(R.menu.action_menu)
                show()
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.action_menu_editar -> {
                    quandoClicaEmEditar(produto)
                    true
                }
                R.id.action_menu_remover -> {
                    quandoClicaEmRemover(produto)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProdutoCardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.vinculador(produto)
    }

    override fun getItemCount(): Int {
        return produtos.size
    }

    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()
    }


    private fun recuperaEstadoCheckbox(chave: Produto): Boolean {
        return lerArquivoDePreferencias().getBoolean(chave.toString(), false)
    }

    private fun salvaEstadoCheckbox(chave: Produto, valor: Boolean) {
        editaArquivoDePreferencias().putBoolean(chave.toString(), valor).commit()
    }

    private fun lerArquivoDePreferencias(): SharedPreferences {
        return context.getSharedPreferences("arquivokotlin", 0)
    }

    private fun editaArquivoDePreferencias(): SharedPreferences.Editor {
        val preferencias = context.getSharedPreferences("arquivokotlin", 0)
        return preferencias.edit()
    }


}
