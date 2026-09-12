package com.example.tarefa7_geradordeapostas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialCardView>(R.id.cardMega).setOnClickListener {
            startActivity(Intent(this, MegaSenaActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardQuina).setOnClickListener {
            startActivity(Intent(this, QuinaActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardLotofacil).setOnClickListener {
            startActivity(Intent(this, LotofacilActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardLotomania).setOnClickListener {
            startActivity(Intent(this, LotomaniaActivity::class.java))
        }
    }
}
