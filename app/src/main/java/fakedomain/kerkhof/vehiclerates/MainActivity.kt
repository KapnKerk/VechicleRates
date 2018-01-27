package fakedomain.kerkhof.vehiclerates

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.google.gson.Gson
import fakedomain.kerkhof.vehiclerates.helpers.Constants
import fakedomain.kerkhof.vehiclerates.model.CreateVehicleRate
import fakedomain.kerkhof.vehiclerates.model.CreateVehicleRateData
import fakedomain.kerkhof.vehiclerates.model.VehicleRatesResponse
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val loadingIndicator: LinearLayout by lazy { findViewById<LinearLayout>(R.id.linLayProgress) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val postButton = findViewById<Button>(R.id.btnPost)
        val getButton = findViewById<Button>(R.id.btnGet)

        postButton.setOnClickListener { createVehicleRate() }
        getButton.setOnClickListener {  getVehicleRates() }
    }

    private fun getVehicleRates() {
        loadingIndicator.visibility = View.VISIBLE

        val authHeader = Credentials.basic(Constants.AUTH_USERNAME, Constants.AUTH_PASS)
        val request = Request.Builder()
                .url(Constants.URL_VEHICLE_RATES)
                .header(Constants.AUTH_NAME, authHeader)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread { loadingIndicator.visibility = View.GONE }

                val gson = Gson()
                val jsonData = response.body()?.string()
                val mappedResponse: VehicleRatesResponse = gson.fromJson(jsonData, VehicleRatesResponse::class.java)

                println(mappedResponse)
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { loadingIndicator.visibility = View.GONE }
                // TODO: Handle failure
            }
        })
    }

    private fun createVehicleRate() {
        loadingIndicator.visibility = View.VISIBLE

        val authHeader = Credentials.basic(Constants.AUTH_USERNAME, Constants.AUTH_PASS)

        val rateData = CreateVehicleRateData(2.1, 3.0)
        val rate = CreateVehicleRate(rateData)
        val gson = Gson()

        val jsonRate = gson.toJson(rate)

        val plainText = MediaType.parse("text")
        val requestBody = RequestBody.create(plainText, jsonRate)

        val request = Request.Builder()
                .url(Constants.URL_VEHICLE_RATES)
                .header(Constants.AUTH_NAME, authHeader)
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread { loadingIndicator.visibility = View.GONE }

                println(response.body()?.string())

            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { loadingIndicator.visibility = View.GONE }
                // TODO: Handle failure
            }
        })
    }
}
