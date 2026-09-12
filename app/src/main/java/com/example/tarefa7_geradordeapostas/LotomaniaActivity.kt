package com.example.tarefa7_geradordeapostas

class LotomaniaActivity : BaseLoteriaActivity() {
    override val nomeLoteria = "Lotomania"
    override val numeroMinimo = 50
    override val numeroMaximo = 50
    override val limiteInferior = 0
    override val limiteSuperior = 99
    override val quantidadeFixa: Int? = 50
    override val corTema = R.color.lotomania_primary
    override val textoRegras = "Regras: cada aposta utiliza exatamente 50 números de 00 a 99. Cada jogo é gerado sem números repetidos, em ordem crescente e sem duplicar outro jogo desta geração."
}
