package com.alicasts.december24.data.repository

import com.alicasts.december24.data.models.ConfirmRideRequest
import com.alicasts.december24.data.models.ConfirmRideResponse
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.data.models.HistoryResponseDriver
import com.alicasts.december24.data.models.RideResponse
import com.alicasts.december24.data.remote.RidesApi
import com.alicasts.december24.presentation.navigation.RoutesArguments.CUSTOMER_ID
import com.alicasts.december24.presentation.navigation.RoutesArguments.DESTINATION
import com.alicasts.december24.presentation.navigation.RoutesArguments.ORIGIN
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class RideOptionsRepositoryImpl @Inject constructor(
    private val api: RidesApi
) : RideOptionsRepository {

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
            throw Exception("An unexpected error occurred.")
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
            ?: "An unknown error occurred while processing the request."
    }

    private fun extractErrorDescription(errorResponse: String?): String {
        return try {
            if (!errorResponse.isNullOrEmpty()) {
                JSONObject(errorResponse).optString("error_description", "Unknown error")
            } else {
                "Unknown error"
            }
        } catch (e: JSONException) {
            "Error parsing error response"
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