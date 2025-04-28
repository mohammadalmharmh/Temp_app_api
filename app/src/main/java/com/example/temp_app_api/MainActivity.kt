package com.example.temp_app_api

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTemperature: TextView
    private val apiKey = ""
    private val city = "Amman"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        textViewTemperature = findViewById(R.id.textViewTemperature)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchTemperature()
    }

    private fun fetchTemperature() {
        val client = OkHttpClient()
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    textViewTemperature.text = "Failed to fetch data"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val json = JSONObject(responseData)
                    val temp = json.getJSONObject("main").getDouble("temp")

                    runOnUiThread {
                        textViewTemperature.text = "Current Temp in $city: $temp°C"
                    }
                } else {
                    runOnUiThread {
                        textViewTemperature.text = "Error: ${response.code}"
                    }
                }
            }
        })
    }
}
