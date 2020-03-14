package com.example.projetocrudprodutos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocrudprodutos.adapter.ProdutoAdapter
import com.example.projetocrudprodutos.model.Produto
import com.example.projetocrudprodutos.retrofit.webClient.ProdutosWebClient

class MainActivity : AppCompatActivity(), ProdutoAdapter.OnItemClickListener {
    private val REQ_CADASTRO = 1;
    private val REQ_DETALHE = 2;
    private var listaProdutos: ArrayList<Produto> = ArrayList()
    private var posicaoAlterar = -1

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ProdutoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val webClient: ProdutosWebClient by lazy {
        ProdutosWebClient()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ProdutoAdapter(listaProdutos)
        viewAdapter.onItemClickListener = this

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {

            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter

        }
        fetchProdutos()
    }

    private fun fetchProdutos() {
        webClient.fetchProdutos(onSuccess = { produtos ->
            produtos?.let {
                it.forEach { produto ->
                    produto?.let {
                        listaProdutos.add(produto)
                    }
                }
                viewAdapter.notifyDataSetChanged()
            }
        }, onFailure = {
            showMessage(it!!)
        })
    }

    override fun onItemClicked(view: View, position: Int) {
        val it = Intent(this, DetalheActivity::class.java)
        this.posicaoAlterar = position
        val produto = listaProdutos.get(position)
        it.putExtra("produto", produto)
        startActivityForResult(it, REQ_DETALHE)
    }

    fun abrirFormulario(view: View) {
        val it = Intent(this, CadastroActivity::class.java)
        startActivityForResult(it, REQ_CADASTRO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CADASTRO) {
            if (resultCode == Activity.RESULT_OK) {
                val produto = data?.getSerializableExtra("produto") as Produto
                webClient.createProduto(produto, onSuccess = {
                    listaProdutos.add(produto)
                    viewAdapter.notifyDataSetChanged()
                    showMessage("Cadastro realizada com sucesso!")
                }, onFailure = {
                    showMessage(it!!)
                })
            }
        } else if (requestCode == REQ_DETALHE) {
            if (resultCode == DetalheActivity.RESULT_EDIT) {
                val produto = data?.getSerializableExtra("produto") as Produto
                webClient.updateProduto(produto, onSuccess = {
                    listaProdutos.set(this.posicaoAlterar, produto)
                    viewAdapter.notifyDataSetChanged()
                    showMessage("Edicao realizada com sucesso!")
                }, onFailure = {
                    showMessage(it!!)
                })
            } else if (resultCode == DetalheActivity.RESULT_DELETE) {
                val produto = listaProdutos[this.posicaoAlterar]
                webClient.deleteProduto(produto.id, onSuccess = {
                    listaProdutos.removeAt(this.posicaoAlterar)
                    viewAdapter.notifyDataSetChanged()
                    showMessage("Exclusao realizada com sucesso!")
                }, onFailure = {
                    showMessage(it!!)
                })
            }
        }
    }
}
