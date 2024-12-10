package com.alicasts.december24.data.repository

import com.alicasts.december24.R
import com.alicasts.december24.data.models.HistoryResponseDriver
import com.alicasts.december24.data.models.Ride
import com.alicasts.december24.data.models.RideHistoryResponse
import com.alicasts.december24.data.remote.RidesApi
import com.alicasts.december24.presentation.navigation.RoutesArguments.DRIVER_ID
import com.alicasts.december24.presentation.navigation.RoutesArguments.RIDE
import com.alicasts.december24.utils.StringResourceProvider
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class RideHistoryRepositoryImpl @Inject constructor(
    private val api: RidesApi,
    stringResourceProvider: StringResourceProvider
) : RideHistoryRepository {

    private val unknownErrorMessage = stringResourceProvider.getString(R.string.unknown_error)
    private val errorDescription = stringResourceProvider.getString(R.string.error_description)
    private val errorParsingTheResponseMessage = stringResourceProvider.getString(R.string.error_parsing_error_response)

    override suspend fun getRideHistory(customerId: String, driverId: String): List<Ride> {
        val route = buildString {
            append("$RIDE/$customerId")
            if (driverId.isNotBlank()) {
                append("?$DRIVER_ID=$driverId")
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
            throw Exception(unknownErrorMessage)
        }
    }

    private fun extractErrorDescription(errorResponse: String?): String {
        return try {
            if (!errorResponse.isNullOrEmpty()) {
                JSONObject(errorResponse).optString(errorDescription)
            } else {
                unknownErrorMessage
            }
        } catch (e: JSONException) {
            errorParsingTheResponseMessage
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