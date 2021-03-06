package fakedomain.kerkhof.vehiclerates.model

/**
 * Created by tkerkhof on 1/21/18.
 */


data class GetVehicleRatesResponse(
        val count: Int,
        val data: ArrayList<VehicleRate>
)

data class VehicleRate(
        val id: Int,
        val rate: Double,
        val wait_time_rate: Double
)

data class CreateVehicleRate(
        val data: CreateVehicleRateData
)

data class CreateVehicleRateData(
        val rate: Double,
        val wait_time_rate: Double
)
