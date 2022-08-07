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
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.resume

fun FusedLocationProviderClient.callback(continuation: Continuation<Coordinate>): LocationCallback {
    return object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            removeLocationUpdates(this)
            val location = result.locations.firstOrNull()
            if (location == null) {
                continuation.resumeWithException(NullPointerException())
            } else {
                continuation.resume(
                    Coordinate(lt = location.latitude, lg = location.longitude)
                )
            }
        }
        override fun onLocationAvailability(availability: LocationAvailability?) {
            removeLocationUpdates(this)
            if (availability == null) {
                continuation.resumeWithException(NullPointerException())
            } else {
                if (!availability.isLocationAvailable) {
                    continuation.resumeWithException(
                        IllegalStateException("Location is not available!")
                    )
                }
            }
        }
    }
}

suspend fun FusedLocationProviderClient.getCoordinate(): Coordinate {
    return suspendCancellableCoroutine<Coordinate> { continuation ->
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                println("on location result...")
                removeLocationUpdates(this)
                val location = result.locations.firstOrNull()
                if (location == null) {
                    continuation.resumeWithException(NullPointerException())
                } else {
                    continuation.resume(
                        Coordinate(lt = location.latitude, lg = location.longitude)
                    )
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
                            IllegalStateException("Location is not available!")
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

class LocationProvider(
    private val context: Context
) {
    private fun getProviderClient(): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context) ?: error(
            "Huawei FusedLocationProviderClient null!"
        )
    }

    fun getCoordinate(): Coordinate {
        return runBlocking {
            withTimeout(15_000L) {
                getProviderClient().getCoordinate()
            }
        }
    }
}
