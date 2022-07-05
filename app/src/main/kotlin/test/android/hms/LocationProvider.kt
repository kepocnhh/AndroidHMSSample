package test.android.hms

import android.content.Context
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationServices
import android.location.Location
import android.os.Looper
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.resume

class LocationProvider(
    private val context: Context
) {
    private fun getProviderClient(): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context) ?: error(
            "Huawei FusedLocationProviderClient null!"
        )
    }

    private suspend fun FusedLocationProviderClient.getCurrentLocation(): Location {
        return suspendCancellableCoroutine<Location> { continuation ->
            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    println("on location result...")
                    removeLocationUpdates(this)
                    val location = result.locations.firstOrNull()
                    if (location == null) {
                        continuation.resumeWithException(NullPointerException())
                    } else {
                        continuation.resume(location)
                    }
                }
                override fun onLocationAvailability(availability: LocationAvailability?) {
                    println("on location availability...")
                    removeLocationUpdates(this)
                    if (availability == null) {
                        continuation.resumeWithException(NullPointerException())
                    } else {
                        if (!availability.isLocationAvailable) {
                            continuation.resumeWithException(
                                IllegalStateException(
                                    """
                                        Location is not available!
                                         - location: ${availability.locationStatus}
                                         - cell: ${availability.cellStatus}
                                         - wifi: ${availability.wifiStatus}
                                    """.trimIndent()
                                )
                            )
                        }
                    }
                }
            }
            val request = LocationRequest()
            request.interval = 1_000
            request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            println("request...")
            requestLocationUpdates(request, callback, Looper.getMainLooper())
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }.result
        }
    }

    fun getLocation(): Location {
        return runBlocking {
            withTimeout(30_000L) {
                getProviderClient().getCurrentLocation()
            }
        }
    }
}
