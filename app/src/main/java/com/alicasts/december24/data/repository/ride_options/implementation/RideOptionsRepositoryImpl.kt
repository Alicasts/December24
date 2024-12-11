package com.alicasts.december24.data.repository.ride_options.implementation

import com.alicasts.december24.R
import com.alicasts.december24.data.models.confirm_ride.ConfirmRideRequest
import com.alicasts.december24.data.models.confirm_ride.ConfirmRideResponse
import com.alicasts.december24.data.models.shared.DriverOption
import com.alicasts.december24.data.models.shared.HistoryResponseDriver
import com.alicasts.december24.data.models.ride_options.RideResponse
import com.alicasts.december24.data.remote.RidesApi
import com.alicasts.december24.data.repository.ride_options.interfaces.RideOptionsRepository
import com.alicasts.december24.presentation.navigation.RoutesArguments.CUSTOMER_ID
import com.alicasts.december24.presentation.navigation.RoutesArguments.DESTINATION
import com.alicasts.december24.presentation.navigation.RoutesArguments.ORIGIN
import com.alicasts.december24.utils.StringResourceProvider
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class RideOptionsRepositoryImpl @Inject constructor(
    private val api: RidesApi,
    stringResourceProvider: StringResourceProvider
) : RideOptionsRepository {

    private val unknownErrorMessage = stringResourceProvider.getString(R.string.unknown_error)
    private val errorDescription = stringResourceProvider.getString(R.string.error_description)
    private val errorParsingTheResponseMessage = stringResourceProvider.getString(R.string.error_parsing_error_response)

    override suspend fun getRideOptions(jsonAsString: String): RideResponse {
        val (customerId, origin, destination) = parseRideOptionsRequestJson(jsonAsString)
        val requestBody = buildRequestBody(
            customerId = customerId,
            origin = origin,
            destination = destination
        )

        return try {
            api.getRideOptions(requestBody)
        } catch (e: HttpException) {
            throw Exception(handleHttpException(e))
        } catch (e: Exception) {
            throw Exception(unknownErrorMessage)
        }
    }

    private fun parseRideOptionsRequestJson(json: String): Triple<String, String, String> {
        val jsonObject = JSONObject(json)
        val customerId = jsonObject.getString(CUSTOMER_ID)
        val origin = jsonObject.getString(ORIGIN)
        val destination = jsonObject.getString(DESTINATION)
        return Triple(customerId, origin, destination)
    }

    private fun buildRequestBody(customerId: String, origin: String, destination: String): Map<String, String> {
        return mapOf(
            CUSTOMER_ID to customerId,
            ORIGIN to origin,
            DESTINATION to destination
        )
    }

    private fun handleHttpException(e: HttpException): String {
        val errorResponse = e.response()?.errorBody()?.string()
        return errorResponse?.let { extractErrorDescription(it) }
            ?: unknownErrorMessage
    }

    private fun extractErrorDescription(errorResponse: String?): String {
        return try {
            if (!errorResponse.isNullOrEmpty()) {
                JSONObject(errorResponse).optString(errorDescription, unknownErrorMessage)
            } else {
                unknownErrorMessage
            }
        } catch (e: JSONException) {
            errorParsingTheResponseMessage
        }
    }

    override suspend fun submitRideRequest(
        customerId: String,
        origin: String,
        destination: String,
        distance: Double,
        duration: String,
        driverOption: DriverOption
    ): ConfirmRideResponse {
        val request = ConfirmRideRequest(
            customerId = customerId,
            origin = origin,
            destination = destination,
            distance = distance,
            duration = duration,
            driver = HistoryResponseDriver(
                id = driverOption.id,
                name = driverOption.name
            ),
            value = driverOption.value
        )
        return api.confirmRide(request)
    }
}