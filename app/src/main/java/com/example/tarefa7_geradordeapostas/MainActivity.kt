package com.example.tarefa7_geradordeapostas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerLoteria: Spinner
    private lateinit var editQuantidadeNumeros: EditText
    private lateinit var editQuantidadeJogos: EditText
    private lateinit var txtResultado: TextView
    private lateinit var txtRegra: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerLoteria = findViewById(R.id.spinnerLoteria)
        editQuantidadeNumeros = findViewById(R.id.editQuantidadeNumeros)
        editQuantidadeJogos = findViewById(R.id.editQuantidadeJogos)
        txtResultado = findViewById(R.id.txtResultado)
        txtRegra = findViewById(R.id.txtRegra)

        val btnGerar = findViewById<Button>(R.id.btnGerar)
        val btnLimpar = findViewById<Button>(R.id.btnLimpar)

        configurarSpinner()

        btnGerar.setOnClickListener {
            gerarJogos()
        }

        btnLimpar.setOnClickListener {
            limparCampos()
        }
    }

    private fun configurarSpinner() {

        val loterias = listOf(
            "Mega-Sena",
            "Quina",
            "Lotofácil",
            "Lotomania"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            loterias
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerLoteria.adapter = adapter

        spinnerLoteria.onItemSelectedListener =
            object : android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    atualizarRegras(loterias[position])
                }

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {
                }
            }
    }

    private fun atualizarRegras(loteria: String) {

        when (loteria) {

            "Mega-Sena" -> {
                txtRegra.text =
                    "Mega-Sena: escolha de 6 a 20 números (1 a 60)"

                editQuantidadeNumeros.isEnabled = true
                editQuantidadeNumeros.setText("6")
            }

            "Quina" -> {
                txtRegra.text =
                    "Quina: escolha de 5 a 15 números (1 a 80)"

                editQuantidadeNumeros.isEnabled = true
                editQuantidadeNumeros.setText("5")
            }

            "Lotofácil" -> {
                txtRegra.text =
                    "Lotofácil: escolha de 15 a 20 números (1 a 25)"

                editQuantidadeNumeros.isEnabled = true
                editQuantidadeNumeros.setText("15")
            }

            "Lotomania" -> {
                txtRegra.text =
                    "Lotomania: 50 números (00 a 99)"

                editQuantidadeNumeros.setText("50")

                // Lotomania sempre utiliza 50 números
                editQuantidadeNumeros.isEnabled = false
            }
        }
    }

    private fun gerarJogos() {

        val loteria = spinnerLoteria.selectedItem.toString()

        val quantidadeNumeros =
            editQuantidadeNumeros.text.toString().toIntOrNull()

        val quantidadeJogos =
            editQuantidadeJogos.text.toString().toIntOrNull()

        if (quantidadeNumeros == null) {

            Toast.makeText(
                this,
                "Informe a quantidade de números.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (quantidadeJogos == null || quantidadeJogos <= 0) {

            Toast.makeText(
                this,
                "Informe uma quantidade válida de jogos.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (!validarQuantidade(loteria, quantidadeNumeros)) {
            return
        }

        val resultado = StringBuilder()

        for (numeroJogo in 1..quantidadeJogos) {

            val numeros = gerarNumeros(
                loteria,
                quantidadeNumeros
            )

            resultado.append("Jogo $numeroJogo\n")

            resultado.append(
                formatarNumeros(loteria, numeros)
            )

            resultado.append("\n\n")
        }

        txtResultado.text = resultado.toString()
    }

    private fun validarQuantidade(
        loteria: String,
        quantidade: Int
    ): Boolean {

        val valido = when (loteria) {

            "Mega-Sena" -> quantidade in 6..20

            "Quina" -> quantidade in 5..15

            "Lotofácil" -> quantidade in 15..20

            "Lotomania" -> quantidade == 50

            else -> false
        }

        if (!valido) {

            val mensagem = when (loteria) {

                "Mega-Sena" ->
                    "Na Mega-Sena escolha entre 6 e 20 números."

                "Quina" ->
                    "Na Quina escolha entre 5 e 15 números."

                "Lotofácil" ->
                    "Na Lotofácil escolha entre 15 e 20 números."

                "Lotomania" ->
                    "Na Lotomania são utilizados 50 números."

                else ->
                    "Quantidade inválida."
            }

            Toast.makeText(
                this,
                mensagem,
                Toast.LENGTH_LONG
            ).show()
        }

        return valido
    }

    private fun gerarNumeros(
        loteria: String,
        quantidade: Int
    ): List<Int> {

        val numerosPossiveis = when (loteria) {

            "Mega-Sena" ->
                (1..60).toList()

            "Quina" ->
                (1..80).toList()

            "Lotofácil" ->
                (1..25).toList()

            "Lotomania" ->
                (0..99).toList()

            else ->
                emptyList()
        }

        return numerosPossiveis
            .shuffled()
            .take(quantidade)
            .sorted()
    }

    private fun formatarNumeros(
        loteria: String,
        numeros: List<Int>
    ): String {

        return if (loteria == "Lotomania") {

            numeros.joinToString(" - ") {
                String.format("%02d", it)
            }

        } else {

            numeros.joinToString(" - ") {
                String.format("%02d", it)
            }
        }
    }

    private fun limparCampos() {

        editQuantidadeJogos.text.clear()
        txtResultado.text = ""

        atualizarRegras(
            spinnerLoteria.selectedItem.toString()
        )
    }
}