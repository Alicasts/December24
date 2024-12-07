package com.alicasts.december24.utils

data class ConfirmRideDriver(val driverId: Int, val minKm: Int)

object Constants {

    const val BASE_URL = "https://xd5zl5kk2yltomvw5fb37y3bm40vsyrx.lambda-url.sa-east-1.on.aws/"

    const val VALID_USER_ID = "CT01"

    val VALID_DRIVERS = listOf(
        ConfirmRideDriver(driverId = 1, minKm = 1),
        ConfirmRideDriver(driverId = 2, minKm = 5),
        ConfirmRideDriver(driverId = 3, minKm = 10)
    )

    val VALID_ADDRESSES = listOf(
        "Av. Pres. Kenedy, 2385 - Remédios, Osasco - SP, 02675-031",
        "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200",
        "Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000",
        "Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001"
    )
}