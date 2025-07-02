// RetrofitClient.kt
package com.example.techtribe

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://trabalhorpa-api.onrender.com/") // com barra final
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
