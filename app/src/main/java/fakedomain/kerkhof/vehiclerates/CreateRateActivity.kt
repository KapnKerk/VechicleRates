package fakedomain.kerkhof.vehiclerates

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import fakedomain.kerkhof.vehiclerates.helpers.Constants
import fakedomain.kerkhof.vehiclerates.model.CreateVehicleRate
import fakedomain.kerkhof.vehiclerates.model.CreateVehicleRateData
import fakedomain.kerkhof.vehiclerates.model.CreateVehicleRateResponse
import okhttp3.*
import java.io.IOException



/**
 * Created by tkerkhof on 2/5/18.
 */
class CreateRateActivity: AppCompatActivity() {

    private val client = OkHttpClient()
    private val loadingIndicator: LinearLayout by lazy { findViewById<LinearLayout>(R.id.linLayProgress) }
    private val authHeader: String by lazy { Credentials.basic(Constants.AUTH_USERNAME, Constants.AUTH_PASS) }

    private lateinit var rateET: EditText
    private lateinit var waitRateET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createrate)

        supportActionBar!!.setTitle("Create Rate")

        rateET = findViewById(R.id.rateET)
        waitRateET = findViewById(R.id.waitRateET)
        val fab: FloatingActionButton = findViewById(R.id.submitRateFAB)

        rateET.requestFocus()
        waitRateET.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                createVehicleRate()
                true
            } else {
                false
            }
        }

        fab.setOnClickListener({ createVehicleRate() })

    }

    private fun createVehicleRate() {
        loadingIndicator.visibility = View.VISIBLE

        val enteredRate: Double = rateET.text.toString().toDouble()
        val enteredWaitRate = waitRateET.text.toString().toDouble()

        val rateData = CreateVehicleRateData(enteredRate, enteredWaitRate)
        val rate = CreateVehicleRate(rateData)

        val jsonRate = Gson().toJson(rate)

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

                val jsonData = response.body()?.string()
                val mappedResponse: CreateVehicleRateResponse = Gson().fromJson(jsonData, CreateVehicleRateResponse::class.java)

                println(mappedResponse)

                //TODO: Validate response

                runOnUiThread { Toast.makeText(this@CreateRateActivity, "Rate created successfully", Toast.LENGTH_SHORT).show() }
                finish()
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { loadingIndicator.visibility = View.GONE }
                // TODO: Handle failure
            }
        })
    }

}