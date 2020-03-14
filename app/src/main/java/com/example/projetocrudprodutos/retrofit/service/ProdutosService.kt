package com.example.projetocrudprodutos.retrofit.service

import com.example.projetocrudprodutos.model.Produto
import retrofit2.Call
import retrofit2.http.*

interface ProdutosService {

    @GET("produtos.json")
    fun fetchProdutos(): Call<List<Produto>>

    @GET("produtos/{id}")
    fun fetchProduto(@Path("id") id: Int): Call<Produto>

    @PUT("produtos/{id}.json")
    fun createProduto(@Path("id") id: Int, @Body produto: Produto): Call<Void>

    @PUT("produtos/{id}.json")
    fun updateProduto(@Path("id") id: Int, @Body produto: Produto): Call<Void>

    @DELETE("produtos/{id}.json")
    fun deleteProduto(@Path("id") id: Int): Call<Void>
}