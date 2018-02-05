package fakedomain.kerkhof.vehiclerates

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.google.gson.Gson
import fakedomain.kerkhof.vehiclerates.helpers.Constants
import fakedomain.kerkhof.vehiclerates.model.GetVehicleRatesResponse
import fakedomain.kerkhof.vehiclerates.model.VehicleRate
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val loadingIndicator: LinearLayout by lazy { findViewById<LinearLayout>(R.id.linLayProgress) }
    private val authHeader: String by lazy { Credentials.basic(Constants.AUTH_USERNAME, Constants.AUTH_PASS) }

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter

    private var vehicleRates: ArrayList<VehicleRate> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab: FloatingActionButton = findViewById(R.id.createRateFAB)
        fab.setOnClickListener {
            val intent = Intent(this, CreateRateActivity::class.java)
            startActivity(intent)
        }

        linearLayoutManager = LinearLayoutManager(this)
        vehicleRateRV.layoutManager = linearLayoutManager
    }

    override fun onStart() {
        super.onStart()
        getVehicleRates()
    }

    private fun refreshRecyclerView() {
        adapter = RecyclerAdapter(vehicleRates)
        vehicleRateRV.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun getVehicleRates() {
        loadingIndicator.visibility = View.VISIBLE
        val request = Request.Builder()
                .url(Constants.URL_VEHICLE_RATES)
                .header(Constants.AUTH_NAME, authHeader)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread { loadingIndicator.visibility = View.GONE }

                val jsonData = response.body()?.string()
                val mappedResponse: GetVehicleRatesResponse = Gson().fromJson(jsonData, GetVehicleRatesResponse::class.java)

                vehicleRates = mappedResponse.data

                runOnUiThread { refreshRecyclerView() }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { loadingIndicator.visibility = View.GONE }
                // TODO: Handle failure
            }
        })
    }

}
