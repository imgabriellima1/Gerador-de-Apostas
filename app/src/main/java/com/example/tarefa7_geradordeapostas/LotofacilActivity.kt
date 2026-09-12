package com.example.tarefa7_geradordeapostas

class LotofacilActivity : BaseLoteriaActivity() {
    override val nomeLoteria = "Lotofácil"
    override val numeroMinimo = 15
    override val numeroMaximo = 20
    override val limiteInferior = 1
    override val limiteSuperior = 25
    override val quantidadeFixa: Int? = null
    override val corTema = R.color.lotofacil_primary
    override val textoRegras = "Regras: escolha entre 15 e 20 números de 01 a 25. Cada jogo é gerado sem números repetidos, em ordem crescente e sem duplicar outro jogo desta geração."
}
