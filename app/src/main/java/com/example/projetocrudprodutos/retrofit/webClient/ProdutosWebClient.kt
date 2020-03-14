package com.example.projetocrudprodutos.retrofit.webClient

import com.example.projetocrudprodutos.model.Produto
import com.example.projetocrudprodutos.retrofit.AppRetrofit
import com.example.projetocrudprodutos.retrofit.service.ProdutosService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ON_FAILURE_MESSAGE = "Something went wrong, try again later!"

class ProdutosWebClient(
    private val produtosService: ProdutosService = AppRetrofit().produtosService
) {

    private fun <T> execute(
        call: Call<T>,
        onSuccess: (data: T?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure(t.message)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onFailure(ON_FAILURE_MESSAGE)
                }
            }
        })
    }

    fun fetchProdutos(
        onSuccess: (produtos: List<Produto>?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        execute(produtosService.fetchProdutos(), onSuccess, onFailure)
    }

    fun createProduto(
        produto: Produto,
        onSuccess: (data: Void?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        execute(produtosService.createProduto(produto.id, produto), onSuccess, onFailure)
    }

    fun updateProduto(
        produto: Produto,
        onSuccess: (data: Void?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        execute(produtosService.updateProduto(produto.id, produto), onSuccess, onFailure)
    }

    fun deleteProduto(
        id: Int,
        onSuccess: (data: Void?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        execute(produtosService.deleteProduto(id), onSuccess, onFailure)
    }
}