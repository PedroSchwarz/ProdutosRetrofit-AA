package com.example.projetocrudprodutos.retrofit

import com.example.projetocrudprodutos.retrofit.service.ProdutosService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://produtosretrofit.firebaseio.com/"

class AppRetrofit {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val produtosService by lazy {
        retrofit.create(ProdutosService::class.java)
    }
}