package com.example.listadecompras.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.listadecompras.adapter.ListaDeProdutosAdapter
import com.example.listadecompras.dao.ProdutoDAO
import com.example.listadecompras.databinding.ActivityListaDeProdutosBinding

class ListaDeProdutosActivity : AppCompatActivity() {

    private val dao = ProdutoDAO()
    private val adapter = ListaDeProdutosAdapter(context = this, produtos =dao.buscaProdutos() )
    private val binding by lazy {ActivityListaDeProdutosBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFAB()



    }

    override fun onResume() {
        super.onResume()
        adapter.atualiza(dao.buscaProdutos())



    }

    fun configuraRecyclerView() {
        val recyclerView = binding.activityListaDeProdutosRecyclerview
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter


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

}