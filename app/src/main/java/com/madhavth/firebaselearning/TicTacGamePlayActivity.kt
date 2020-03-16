package com.madhavth.firebaselearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.tic_tac_gameplay.*

class TicTacGamePlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tic_tac_gameplay)

        btnRestTicTac.setOnClickListener {
            ticTacCustomView.resetGame()
        }

        btnswitchTicTacOpponent.setOnClickListener {
            //todo
            Toast.makeText(applicationContext
            , "bot is busy right now..",
            Toast.LENGTH_SHORT).show()
        }
    }
}
