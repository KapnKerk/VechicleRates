package fakedomain.kerkhof.vehiclerates.model

/**
 * Created by tkerkhof on 1/21/18.
 */


data class VehicleRatesResponse(
        val count: Int,
        val data: List<VehicleRate>
)

data class VehicleRate(
        val id: Int,
        val rate: Double,
        val wait_time_rate: Double
)
