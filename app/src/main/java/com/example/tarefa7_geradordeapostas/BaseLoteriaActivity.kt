package com.example.tarefa7_geradordeapostas

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

abstract class BaseLoteriaActivity : AppCompatActivity() {

    abstract val nomeLoteria: String
    abstract val numeroMinimo: Int
    abstract val numeroMaximo: Int
    abstract val limiteInferior: Int
    abstract val limiteSuperior: Int
    abstract val quantidadeFixa: Int?
    abstract val textoRegras: String
    @get:ColorRes abstract val corTema: Int

    private lateinit var editQuantidadeNumeros: TextInputEditText
    private lateinit var editQuantidadeJogos: TextInputEditText
    private lateinit var layoutQuantidadeNumeros: TextInputLayout
    private lateinit var layoutQuantidadeJogos: TextInputLayout
    private lateinit var txtResultado: TextView
    private lateinit var btnCopiar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loteria)

        configurarCabecalho()
        configurarCampos()
        configurarAcoes()
    }

    private fun configurarCabecalho() {
        findViewById<TextView>(R.id.txtTitulo).text = nomeLoteria
        findViewById<TextView>(R.id.txtDescricao).text = descricaoCurta()
        findViewById<TextView>(R.id.txtRegras).text = textoRegras
        findViewById<MaterialCardView>(R.id.cardCabecalho).setCardBackgroundColor(getColor(corTema))
        findViewById<MaterialButton>(R.id.btnGerar).setBackgroundColor(getColor(corTema))
    }

    private fun configurarCampos() {
        editQuantidadeNumeros = findViewById(R.id.editQuantidadeNumeros)
        editQuantidadeJogos = findViewById(R.id.editQuantidadeJogos)
        layoutQuantidadeNumeros = findViewById(R.id.layoutQuantidadeNumeros)
        layoutQuantidadeJogos = findViewById(R.id.layoutQuantidadeJogos)
        txtResultado = findViewById(R.id.txtResultado)
        btnCopiar = findViewById(R.id.btnCopiar)

        editQuantidadeJogos.setText("1")

        if (quantidadeFixa != null) {
            editQuantidadeNumeros.setText(quantidadeFixa.toString())
            editQuantidadeNumeros.isEnabled = false
            layoutQuantidadeNumeros.helperText = "Quantidade fixa para esta loteria"
        } else {
            editQuantidadeNumeros.setText(numeroMinimo.toString())
            layoutQuantidadeNumeros.helperText = "Permitido: $numeroMinimo a $numeroMaximo números"
        }
    }

    private fun configurarAcoes() {
        findViewById<TextView>(R.id.btnVoltar).setOnClickListener { finish() }

        findViewById<MaterialButton>(R.id.btnGerar).setOnClickListener {
            gerarApostas()
        }

        findViewById<MaterialButton>(R.id.btnLimpar).setOnClickListener {
            limparTela()
        }

        btnCopiar.setOnClickListener {
            copiarResultado()
        }
    }

    private fun gerarApostas() {
        limparErros()

        val quantidadeNumeros = editQuantidadeNumeros.text?.toString()?.trim()?.toIntOrNull()
        val quantidadeJogos = editQuantidadeJogos.text?.toString()?.trim()?.toIntOrNull()

        var valido = true

        if (quantidadeNumeros == null) {
            layoutQuantidadeNumeros.error = "Informe a quantidade de números."
            valido = false
        } else if (quantidadeFixa != null && quantidadeNumeros != quantidadeFixa) {
            layoutQuantidadeNumeros.error = "Esta loteria exige exatamente $quantidadeFixa números."
            valido = false
        } else if (quantidadeFixa == null && quantidadeNumeros !in numeroMinimo..numeroMaximo) {
            layoutQuantidadeNumeros.error = "Digite um valor entre $numeroMinimo e $numeroMaximo."
            valido = false
        }

        if (quantidadeJogos == null) {
            layoutQuantidadeJogos.error = "Informe a quantidade de jogos."
            valido = false
        } else if (quantidadeJogos !in 1..100) {
            layoutQuantidadeJogos.error = "Digite uma quantidade entre 1 e 100 jogos."
            valido = false
        }

        if (!valido || quantidadeNumeros == null || quantidadeJogos == null) return

        val jogos = gerarJogosUnicos(quantidadeNumeros, quantidadeJogos)

        if (jogos.size < quantidadeJogos) {
            Toast.makeText(
                this,
                "Não foi possível gerar todos os jogos sem duplicidade. Tente uma quantidade menor.",
                Toast.LENGTH_LONG
            ).show()
        }

        txtResultado.text = jogos.mapIndexed { index, jogo ->
            "Jogo ${index + 1}: ${formatarJogo(jogo)}"
        }.joinToString("\n\n")

        txtResultado.setTextColor(getColor(R.color.text_primary))
        btnCopiar.isEnabled = jogos.isNotEmpty()
    }

    private fun gerarJogosUnicos(quantidadeNumeros: Int, quantidadeJogos: Int): List<List<Int>> {
        val jogos = linkedSetOf<List<Int>>()
        var tentativas = 0
        val maxTentativas = quantidadeJogos * 50

        while (jogos.size < quantidadeJogos && tentativas < maxTentativas) {
            val jogo = (limiteInferior..limiteSuperior)
                .shuffled()
                .take(quantidadeNumeros)
                .sorted()

            jogos.add(jogo)
            tentativas++
        }

        return jogos.toList()
    }

    private fun formatarJogo(numeros: List<Int>): String {
        return numeros.joinToString("  ") { numero ->
            String.format("%02d", numero)
        }
    }

    private fun limparTela() {
        limparErros()
        txtResultado.text = "Seus jogos aparecerão aqui."
        txtResultado.setTextColor(getColor(R.color.text_secondary))
        btnCopiar.isEnabled = false
        editQuantidadeJogos.setText("1")

        quantidadeFixa?.let {
            editQuantidadeNumeros.setText(it.toString())
        } ?: editQuantidadeNumeros.setText(numeroMinimo.toString())
    }

    private fun limparErros() {
        layoutQuantidadeNumeros.error = null
        layoutQuantidadeJogos.error = null
    }

    private fun copiarResultado() {
        val texto = txtResultado.text.toString()
        if (texto.isBlank() || texto == "Seus jogos aparecerão aqui.") return

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Jogos $nomeLoteria", texto))
        Toast.makeText(this, "Jogos copiados.", Toast.LENGTH_SHORT).show()
    }

    private fun descricaoCurta(): String {
        val faixa = if (limiteInferior == 0) "00 a ${String.format("%02d", limiteSuperior)}" else
            "${String.format("%02d", limiteInferior)} a ${String.format("%02d", limiteSuperior)}"

        return if (quantidadeFixa != null) {
            "$quantidadeFixa números • $faixa"
        } else {
            "$numeroMinimo a $numeroMaximo números • $faixa"
        }
    }
}
