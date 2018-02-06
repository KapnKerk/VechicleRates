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
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by tkerkhof on 2/5/18.
 */
class CreateRateActivity: AppCompatActivity() {

    private val loadingIndicator: LinearLayout by lazy { findViewById<LinearLayout>(R.id.linLayProgress) }
    private val authHeader: String by lazy { Credentials.basic(Constants.AUTH_USERNAME, Constants.AUTH_PASS) }

    private lateinit var rateET: EditText
    private lateinit var waitRateET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createrate)

        supportActionBar!!.title = Constants.CREATE_RATE_TITLE

        rateET = findViewById(R.id.rateET)
        waitRateET = findViewById(R.id.waitRateET)
        val fab: FloatingActionButton = findViewById(R.id.submitRateFAB)

        rateET.requestFocus()
        waitRateET.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                validateInput()
                true
            } else {
                false
            }
        }

        fab.setOnClickListener({ validateInput() })

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
        loadingIndicator.visibility = View.VISIBLE

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
                runOnUiThread { loadingIndicator.visibility = View.GONE }

                if (response.code() == 200 || response.code() == 201) {
                    runOnUiThread { Toast.makeText(this@CreateRateActivity, Constants.CREATE_RATE_SUCCESS_TOAST, Toast.LENGTH_SHORT).show() }
                    finish()
                } else {
                    // TODO: Handle failure
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { loadingIndicator.visibility = View.GONE }
                // TODO: Handle failure
            }
        })
    }

}