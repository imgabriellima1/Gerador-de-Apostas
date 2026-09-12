package com.example.tarefa7_geradordeapostas

class QuinaActivity : BaseLoteriaActivity() {
    override val nomeLoteria = "Quina"
    override val numeroMinimo = 5
    override val numeroMaximo = 15
    override val limiteInferior = 1
    override val limiteSuperior = 80
    override val quantidadeFixa: Int? = null
    override val corTema = R.color.quina_primary
    override val textoRegras = "Regras: escolha entre 5 e 15 números de 01 a 80. Cada jogo é gerado sem números repetidos, em ordem crescente e sem duplicar outro jogo desta geração."
}
