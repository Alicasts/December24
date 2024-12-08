package com.alicasts.december24.data.repository

import com.alicasts.december24.data.models.TravelResponse
import com.alicasts.december24.data.remote.RidesApi
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class TravelOptionsRepositoryImpl @Inject constructor(
    private val api: RidesApi
) : TravelOptionsRepository {

    override suspend fun getTravelOptions(customerId: String, origin: String, destination: String): TravelResponse {
        val requestBody = mapOf(
            "customer_id" to customerId,
            "origin" to origin,
            "destination" to destination
        )
        return try {
            api.getTravelOptions(requestBody)
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
}