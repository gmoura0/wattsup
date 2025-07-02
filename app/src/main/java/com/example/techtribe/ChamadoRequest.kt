// ChamadoRequest.kt
package com.example.techtribe

data class ChamadoRequest(
    val local_subestacao: String,
    val nome_tecnico: String,
    val acao_tomada: String,
    val gravidade: String,
    val situacao_subestacao: String
)
