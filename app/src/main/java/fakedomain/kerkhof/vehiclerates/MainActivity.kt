package fakedomain.kerkhof.vehiclerates

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fakedomain.kerkhof.vehiclerates.helpers.Constants
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val postButton = findViewById<Button>(R.id.post_button)
        val getButton = findViewById<Button>(R.id.get_button)

        postButton.setOnClickListener { getVehicleRates() }
        getButton.setOnClickListener {  createVehicleRate() }
    }

    private fun getVehicleRates() {
        val authHeader = Credentials.basic(Constants.AUTH_USERNAME, Constants.AUTH_PASS)
        val request = Request.Builder()
                .url(Constants.URL_VEHICLE_RATES)
                .header(Constants.AUTH_NAME, authHeader)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
        })
    }

    private fun createVehicleRate() {
        // TODO: write create vehicle rate service
    }
}
