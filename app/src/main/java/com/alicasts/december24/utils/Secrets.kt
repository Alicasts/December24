package com.alicasts.december24.utils

object Secrets {

    /**
     * This Mapbox token is valid until 12/22/2024.
     * After this date, it will be revoked for security reasons.
     * If a new token is needed, generate one in the Mapbox dashboard:
     * https://account.mapbox.com/
     *
     * NOTE: Sensitive tokens should never be exposed in public projects.
     * This token is exposed only for development purposes and will be revoked when necessary.
     */

    const val MAPBOX_API_KEY = "sk.eyJ1IjoiYWxpY2FzdCIsImEiOiJjbTRlb3V6aTAwajN3MmpwbzR3ZGI0eW9vIn0.D1nhSAlvOEAkQSn0hM5HAA"
}