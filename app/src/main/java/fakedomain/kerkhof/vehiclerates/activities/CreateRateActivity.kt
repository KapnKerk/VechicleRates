package fakedomain.kerkhof.vehiclerates.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.gson.Gson
import fakedomain.kerkhof.vehiclerates.R
import fakedomain.kerkhof.vehiclerates.helpers.Constants
import fakedomain.kerkhof.vehiclerates.model.CreateVehicleRate
import fakedomain.kerkhof.vehiclerates.model.CreateVehicleRateData
import kotlinx.android.synthetic.main.activity_createrate.*
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by tkerkhof on 2/5/18.
 */
class CreateRateActivity: AppCompatActivity() {

    private val authHeader: String by lazy { Credentials.basic(Constants.AUTH_USERNAME, Constants.AUTH_PASS) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createrate)

        setupUI()
    }

    private fun setupUI() {
        supportActionBar!!.title = Constants.CREATE_RATE_TITLE

        rateET.requestFocus()
        waitRateET.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                validateInput()
                true
            } else {
                false
            }
        }

        submitRateFAB.setOnClickListener({ validateInput() })
    }

    private fun validateInput() {

        val rateText = rateET.text.toString()
        val waitRateText = waitRateET.text.toString()

        if (rateText.isEmpty() || waitRateText.isEmpty()) {
            Toast.makeText(this, Constants.ENTER_NUMERIC_VAL, Toast.LENGTH_SHORT).show()
        } else {
            val enteredRate = rateText.toDouble()
            val enteredWaitRate = waitRateText.toDouble()

            createVehicleRate(enteredRate, enteredWaitRate)
        }
    }

    private fun createVehicleRate(enteredRate: Double, enteredWaitRate: Double) {
        createRateProgress.visibility = View.VISIBLE

        val rateData = CreateVehicleRateData(enteredRate, enteredWaitRate)
        val rate = CreateVehicleRate(rateData)

        val jsonRate = Gson().toJson(rate)

        val plainText = MediaType.parse("text")
        val requestBody = RequestBody.create(plainText, jsonRate)

        val client: OkHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build()

        val request = Request.Builder()
                .url(Constants.URL_VEHICLE_RATES)
                .header(Constants.AUTH_NAME, authHeader)
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread { createRateProgress.visibility = View.GONE }

                when (response.code()) {
                    200, 201 -> {
                        runOnUiThread { Toast.makeText(this@CreateRateActivity, Constants.CREATE_RATE_SUCCESS_TOAST, Toast.LENGTH_SHORT).show() }
                        finish()
                    }
                    500 -> {
                        runOnUiThread { Toast.makeText(this@CreateRateActivity, Constants.ERROR_500_TEXT, Toast.LENGTH_SHORT).show() }
                    }
                    else -> {
                        runOnUiThread { createNoConnectionTV.visibility = View.VISIBLE }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    createRateProgress.visibility = View.GONE
                    createNoConnectionTV.visibility = View.VISIBLE
                }
            }
        })
    }

}