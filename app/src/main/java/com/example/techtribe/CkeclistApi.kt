package com.example.techtribe

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChecklistApi {
    @FormUrlEncoded
    @POST("chamado/")
    fun enviarChamado(
        @Field("local_subestacao") localSubestacao: String,
        @Field("nome_tecnico") nomeTecnico: String,
        @Field("acao_tomada") acaoTomada: String,
        @Field("gravidade") gravidade: String,
        @Field("situacao_subestacao") situacaoSubestacao: String
    ): Call<Void>
}
