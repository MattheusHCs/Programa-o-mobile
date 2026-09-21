package com.example.meucurso

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Tela 2 - Sobre o Curso
 * Mostra detalhes do curso (duração, modalidade, descrição).
 * Permite voltar para a Home ou avançar para Disciplinas.
 */
class SobreCursoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sobre_curso)

        val btnVoltar = findViewById<Button>(R.id.btnVoltar)
        val btnDisciplinas = findViewById<Button>(R.id.btnDisciplinasSobre)

        // finish() fecha esta Activity e retorna à tela anterior (Home),
        // que continua na pilha de navegação
        btnVoltar.setOnClickListener {
            finish()
        }

        // Navega para a tela de Disciplinas
        btnDisciplinas.setOnClickListener {
            val intent = Intent(this, DisciplinasActivity::class.java)
            startActivity(intent)
        }
    }
}
