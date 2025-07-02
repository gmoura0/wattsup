// ChamadoService.kt
package com.example.techtribe

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChamadoService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("chamado/") // sem barra no início para concatenar corretamente com baseUrl
    fun enviarChamado(@Body chamadoRequest: ChamadoRequest): Call<Void>
}
