package com.example.meucurso

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Tela 1 - Home
 * Apresenta a instituição, o curso e dois botões que navegam
 * para as demais telas do aplicativo usando Intent explícita.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnConhecaCurso = findViewById<Button>(R.id.btnConhecaCurso)
        val btnDisciplinas = findViewById<Button>(R.id.btnDisciplinas)

        // Ao clicar, abre a tela "Sobre o Curso"
        btnConhecaCurso.setOnClickListener {
            val intent = Intent(this, SobreCursoActivity::class.java)
            startActivity(intent)
        }

        // Ao clicar, abre a tela "Disciplinas" diretamente a partir da Home
        btnDisciplinas.setOnClickListener {
            val intent = Intent(this, DisciplinasActivity::class.java)
            startActivity(intent)
        }
    }
}
