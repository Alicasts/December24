package com.alicasts.december24.data.repository

import com.alicasts.december24.data.models.ConfirmRideRequest
import com.alicasts.december24.data.models.ConfirmRideResponse
import com.alicasts.december24.data.models.DriverOption
import com.alicasts.december24.data.models.HistoryResponseDriver
import com.alicasts.december24.data.models.TravelResponse
import com.alicasts.december24.data.remote.RidesApi
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class TravelOptionsRepositoryImpl @Inject constructor(
    private val api: RidesApi
) : TravelOptionsRepository {

    override suspend fun getTravelOptions(jsonAsString: String): TravelResponse {
        val (customerId, origin, destination) = parseTravelOptionsRequestJson(jsonAsString)
        val requestBody = buildRequestBody(
            customerId = customerId,
            origin = origin,
            destination = destination
        )

        return try {
            api.getTravelOptions(requestBody)
        } catch (e: HttpException) {
            throw Exception(handleHttpException(e))
        } catch (e: Exception) {
            throw Exception("An unexpected error occurred.")
        }
    }

    private fun parseTravelOptionsRequestJson(json: String): Triple<String, String, String> {
        val jsonObject = JSONObject(json)
        val customerId = jsonObject.getString("customer_id")
        val origin = jsonObject.getString("origin")
        val destination = jsonObject.getString("destination")
        return Triple(customerId, origin, destination)
    }

    private fun buildRequestBody(customerId: String, origin: String, destination: String): Map<String, String> {
        return mapOf(
            "customer_id" to customerId,
            "origin" to origin,
            "destination" to destination
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