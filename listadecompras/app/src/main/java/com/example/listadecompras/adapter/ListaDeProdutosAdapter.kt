package com.example.listadecompras.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.listadecompras.databinding.ProdutoCardBinding
import com.example.listadecompras.model.Produto

class ListaDeProdutosAdapter(
    produtos: List<Produto>,
    private val context: Context
) :Adapter<ListaDeProdutosAdapter.ViewHolder>(){

    private val produtos = produtos.toMutableList()

    class ViewHolder(private val binding: ProdutoCardBinding)
        :RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener {
                val card = binding.card
                card.isChecked = !card.isChecked
            }
        }

            fun vinculador(produto: Produto){
                val nome = binding.produtoCardNome
                nome.text = produto.nome
                val quantidade = binding.produtoCardQuantidade
                quantidade.text = produto.quantidade
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProdutoCardBinding.inflate(inflater,parent,false)
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


}
