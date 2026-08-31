package com.example.meucurso

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Tela 3 - Disciplinas
 * Lista as disciplinas do curso. O botão "Voltar" apenas
 * finaliza a Activity atual, retornando para a tela anterior
 * (que pode ser a Home ou a tela Sobre o Curso, dependendo
 * de onde o usuário veio).
 */
class DisciplinasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disciplinas)

        val btnVoltar = findViewById<Button>(R.id.btnVoltarDisciplinas)

        btnVoltar.setOnClickListener {
            finish()
        }
    }
}
