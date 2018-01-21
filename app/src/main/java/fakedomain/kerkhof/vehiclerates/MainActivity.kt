package fakedomain.kerkhof.vehiclerates

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var postButton = findViewById<Button>(R.id.post_button)
        var getButton = findViewById<Button>(R.id.get_button)

        postButton.setOnClickListener { testMethod1() }
        getButton.setOnClickListener {  testMethod2() }
    }

    private fun testMethod1() {
        var cat = 0;
    }

    private fun testMethod2() {
        var cat = 2;
    }
}
