package com.alicasts.december24.data.repository

import com.alicasts.december24.data.models.HistoryResponseDriver
import com.alicasts.december24.data.models.Ride
import com.alicasts.december24.data.models.RideHistoryResponse
import com.alicasts.december24.data.remote.RidesApi
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class RideHistoryRepositoryImpl @Inject constructor(
    private val api: RidesApi
) : RideHistoryRepository {

    override suspend fun getRideHistory(customerId: String, driverId: String): List<Ride> {
        val route = buildString {
            append("ride/$customerId")
            if (driverId.isNotBlank()) {
                append("?driver_id=$driverId")
            }
        }
        return try {
            val response = api.getRide(route)
            mapToRideUiModel(response)
        } catch (e: HttpException) {
            val errorResponse = e.response()?.errorBody()?.string()
            val errorMessage = extractErrorDescription(errorResponse)
            throw Exception(errorMessage)
        } catch (e: Exception) {
            throw Exception("An unexpected error occurred.")
        }
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

    private fun mapToRideUiModel(response: RideHistoryResponse): List<Ride> {
        return response.rides.map { ride ->
            Ride(
                id = ride.id,
                date = ride.date,
                origin = ride.origin,
                destination = ride.destination,
                driver  = ride.driver.let {
                    HistoryResponseDriver(
                        id = it.id,
                        name = it.name
                    )
                },
                value = ride.value,
                distance = ride.distance,
                duration = ride.duration
            )
        }
    }
}