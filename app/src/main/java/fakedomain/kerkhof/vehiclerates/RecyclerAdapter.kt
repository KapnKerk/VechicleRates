package fakedomain.kerkhof.vehiclerates


import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import fakedomain.kerkhof.vehiclerates.helpers.Constants
import fakedomain.kerkhof.vehiclerates.helpers.inflate
import fakedomain.kerkhof.vehiclerates.model.VehicleRate
import kotlinx.android.synthetic.main.vehiclerate_item_row.view.*

/**
* Created by tkerkhof on 1/28/18.
*/

class RecyclerAdapter(private val vehicleRates: ArrayList<VehicleRate>) : RecyclerView.Adapter<RecyclerAdapter.RateViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.RateViewHolder {
        val inflatedView = parent.inflate(R.layout.vehiclerate_item_row, false)
        return RateViewHolder(inflatedView)
    }

    override fun getItemCount() = vehicleRates.size

    override fun onBindViewHolder(holder: RecyclerAdapter.RateViewHolder, position: Int) {
        val itemRate = vehicleRates[position]
        holder.bindRate(itemRate)
    }

    class RateViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var rate: VehicleRate? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            //TODO: Implement, if needed
        }

        fun bindRate(rate: VehicleRate) {
            this.rate = rate

            view.vehiclerateId.text = String.format(Constants.RATE_ID_PREFIX, rate.id)
            view.vehicleRate.text = String.format(Constants.RATE_PREFIX, rate.rate)
            view.vehicleWaitTimeRate.text = String.format(Constants.WAIT_TIME_RATE_PREFIX, rate.wait_time_rate)
        }
    }

}

