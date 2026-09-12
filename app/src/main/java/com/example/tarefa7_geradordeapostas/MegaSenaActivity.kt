package com.example.tarefa7_geradordeapostas

class MegaSenaActivity : BaseLoteriaActivity() {
    override val nomeLoteria = "Mega-Sena"
    override val numeroMinimo = 6
    override val numeroMaximo = 20
    override val limiteInferior = 1
    override val limiteSuperior = 60
    override val quantidadeFixa: Int? = null
    override val corTema = R.color.mega_primary
    override val textoRegras = "Regras: escolha entre 6 e 20 números de 01 a 60. Cada jogo é gerado sem números repetidos, em ordem crescente e sem duplicar outro jogo desta geração."
}
