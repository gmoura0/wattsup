package com.example.techtribe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {

    private lateinit var api: ChecklistApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configura Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://trabalhorpa-api.onrender.com/")  // sua URL base
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ChecklistApi::class.java)

        // Referências das views
        val editTextLocal = findViewById<EditText>(R.id.editTextLocal)
        val editTextNomeTecnico = findViewById<EditText>(R.id.editTextNomeTecnico)
        val radioGroupSituacao = findViewById<RadioGroup>(R.id.radioGroupSituacao)
        val textViewLabelAcaoTomada = findViewById<TextView>(R.id.textViewLabelAcaoTomada)
        val editTextAcaoTomada = findViewById<EditText>(R.id.editTextAcaoTomada)
        val radioGroupGravidade = findViewById<RadioGroup>(R.id.radioGroupGravidade)
        val textViewLabelGravidade = findViewById<TextView>(R.id.textViewLabelGravidade)
        val buttonSalvar = findViewById<Button>(R.id.buttonSalvar)

        // Mostrar/ocultar campos com base na situação selecionada
        radioGroupSituacao.setOnCheckedChangeListener { _, checkedId ->
            val isCritica = checkedId == R.id.radioButtonCritica
            val visibility = if (isCritica) View.VISIBLE else View.GONE

            textViewLabelAcaoTomada.visibility = visibility
            editTextAcaoTomada.visibility = visibility
            textViewLabelGravidade.visibility = visibility
            radioGroupGravidade.visibility = visibility
        }

        // Ação do botão salvar
        buttonSalvar.setOnClickListener {
            val local = editTextLocal.text.toString().trim()
            val nomeTecnico = editTextNomeTecnico.text.toString().trim()
            val situacaoId = radioGroupSituacao.checkedRadioButtonId
            val situacaoSelecionada = if (situacaoId != -1) findViewById<RadioButton>(situacaoId).text.toString() else ""
            val acaoTomada = editTextAcaoTomada.text.toString().trim()
            val gravidadeId = radioGroupGravidade.checkedRadioButtonId
            val gravidadeSelecionada = if (gravidadeId != -1) findViewById<RadioButton>(gravidadeId).text.toString() else ""

            // Validações básicas
            if (local.isEmpty()) {
                editTextLocal.error = "Campo obrigatório"
                Toast.makeText(this, "Preencha o campo Local", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (nomeTecnico.isEmpty()) {
                editTextNomeTecnico.error = "Campo obrigatório"
                Toast.makeText(this, "Preencha o nome do técnico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (situacaoSelecionada.isEmpty()) {
                Toast.makeText(this, "Selecione a situação da subestação", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (situacaoSelecionada == "Crítica") {
                if (acaoTomada.isEmpty()) {
                    editTextAcaoTomada.error = "Campo obrigatório"
                    Toast.makeText(this, "Preencha a ação tomada", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (gravidadeSelecionada.isEmpty()) {
                    Toast.makeText(this, "Selecione a gravidade", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Chamada Retrofit para enviar os dados
            api.enviarChamado(
                local,
                nomeTecnico,
                if (situacaoSelecionada == "Crítica") acaoTomada else "",
                if (situacaoSelecionada == "Crítica") gravidadeSelecionada else "",
                situacaoSelecionada
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Checklist enviado com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("API", "Erro: ${response.code()} - ${response.message()}. Body: $errorBody")
                        Toast.makeText(this@MainActivity, "Erro ao enviar checklist! Código: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }


                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Falha na conexão: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                    Log.e("API", "Falha: ${t.message}")
                }
            })
        }
    }
}
