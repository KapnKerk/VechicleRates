package fakedomain.kerkhof.vehiclerates.helpers

/**
 * Created by tkerkhof on 1/21/18.
 */
object Constants {
    const val CREATE_RATE_TITLE = "Create Rate"


    // Networking
    const val URL_VEHICLE_RATES = "https://go-denver-staging.eliteextra.com/x/api/vehiclerates"
    const val AUTH_NAME = "Authorization"
    const val AUTH_USERNAME = "adc-developer-test"
    const val AUTH_PASS = "2534f4ddaabb6a126dc84645a181ae85"

    // Labels
    const val RATE_ID_PREFIX = "Rate #%s"
    const val RATE_PREFIX = "Vehicle Rate: %s"
    const val WAIT_TIME_RATE_PREFIX = "Wait Time Rate: %s"

    // Validation/Error Handling
    const val CREATE_RATE_SUCCESS_TOAST = "Rate created successfully"
    const val ENTER_NUMERIC_VAL = "Please enter numeric rates"
    const val ERROR_500_TEXT = "This feature is currently unavailable."
}