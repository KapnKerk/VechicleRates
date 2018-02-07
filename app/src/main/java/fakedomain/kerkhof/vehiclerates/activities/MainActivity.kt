package fakedomain.kerkhof.vehiclerates.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import fakedomain.kerkhof.vehiclerates.R
import fakedomain.kerkhof.vehiclerates.RecyclerAdapter
import fakedomain.kerkhof.vehiclerates.helpers.Constants
import fakedomain.kerkhof.vehiclerates.model.GetVehicleRatesResponse
import fakedomain.kerkhof.vehiclerates.model.VehicleRate
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val authHeader: String by lazy { Credentials.basic(Constants.AUTH_USERNAME, Constants.AUTH_PASS) }

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter

    private var vehicleRates: ArrayList<VehicleRate> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
    }

    override fun onStart() {
        super.onStart()
        getVehicleRates()
    }

    private fun setupUI() {
        createRateFAB.setOnClickListener {
            val intent = Intent(this, CreateRateActivity::class.java)
            startActivity(intent)
        }

        swipeRefresh.setOnRefreshListener { getVehicleRates() }

        linearLayoutManager = LinearLayoutManager(this)
        vehicleRateRV.layoutManager = linearLayoutManager
    }

    private fun refreshRecyclerView() {
        adapter = RecyclerAdapter(vehicleRates)
        vehicleRateRV.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun getVehicleRates() {
        swipeRefresh.isRefreshing = true

        val client: OkHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build()

        val request = Request.Builder()
                .url(Constants.URL_VEHICLE_RATES)
                .header(Constants.AUTH_NAME, authHeader)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    swipeRefresh.isRefreshing = false
                    mainNoConnectionTV.visibility = View.GONE
                }

                when (response.code()) {
                    200, 201 -> {
                        val jsonData = response.body()?.string()
                        val mappedResponse: GetVehicleRatesResponse = Gson().fromJson(jsonData, GetVehicleRatesResponse::class.java)

                        vehicleRates = mappedResponse.data

                        runOnUiThread { refreshRecyclerView() }
                    }
                    500 -> {
                        runOnUiThread { Toast.makeText(this@MainActivity, Constants.ERROR_500_TEXT, Toast.LENGTH_SHORT).show() }
                    }
                    else -> {
                        runOnUiThread { mainNoConnectionTV.visibility = View.VISIBLE }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    swipeRefresh.isRefreshing = false
                    mainNoConnectionTV.visibility = View.VISIBLE
                    refreshRecyclerView()
                }
            }
        })
    }

}
