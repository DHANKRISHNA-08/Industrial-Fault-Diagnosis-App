package com.dhan.industrialfaultdiagnosis

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.os.Handler
import kotlin.random.Random

class ScadaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scada)

        val txtTank = findViewById<TextView>(R.id.txtTank)
        val txtTemp = findViewById<TextView>(R.id.txtTemp)
        val txtFlow = findViewById<TextView>(R.id.txtFlow)
        val txtAlarm = findViewById<TextView>(R.id.txtAlarm)

        val handler = Handler()

        val runnable = object : Runnable {
            override fun run() {

                val tankLevel = Random.nextInt(65, 90)
                val temperature = Random.nextInt(55, 80)
                val flowRate = Random.nextInt(100, 150)

                txtTank.text = "Tank Level : $tankLevel%"
                txtTemp.text = "Temperature : $temperature°C"
                txtFlow.text = "Flow Rate : $flowRate L/min"
                if (temperature > 75) {
                    txtAlarm.text = "ACTIVE ALARM : HIGH TEMPERATURE"
                }
                else if (temperature >= 65) {
                    txtAlarm.text = "ACTIVE ALARM : WARNING"
                }
                else {
                    txtAlarm.text = "ACTIVE ALARM : NORMAL"
                }

                handler.postDelayed(this, 2000)
            }
        }

        handler.post(runnable)

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        val btnAlarmHistory = findViewById<Button>(R.id.btnAlarmHistory)

        btnAlarmHistory.setOnClickListener {

            val intent = Intent(
                this,
                AlarmActivity::class.java
            )

            startActivity(intent)
        }
    }
}