package dev.viviantung.myruns

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import androidx.core.content.ContextCompat.registerReceiver
class TrackingService: Service(), LocationListener  {
    private val PERMISSION_REQUEST_CODE = 0
    private lateinit var locationManager: LocationManager
//    private var mapCentered = false

//    private lateinit var statsView: TextView

    private lateinit var  myBinder: MyBinder
    private var msgHandler: Handler? = null


    // bind things
    private val PENDINGINTENT_REQUEST_CODE = 0
    private val NOTIFY_ID = 11
    private val CHANNEL_ID = "notification channel"
    private lateinit var myBroadcastReceiver: MyBroadcastReceiver
    private lateinit var notificationManager: NotificationManager

    companion object{
        val STOP_SERVICE_ACTION = "stop service action"
        val INT_KEY = "int key"
        val MSG_INT_VALUE = 0
    }

    val pathPoints = MutableLiveData<MutableList<LatLng>>(mutableListOf())
//    val stats = MutableLiveData<TrackingStats>()

    private var lastLocation: Location? = null
    private var totalDistance = 0.0


    override fun onCreate() {
        super.onCreate()
        // set up notif
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel() // ensure channel exists first
        startForeground(NOTIFY_ID, buildNotification())

        // register broadcast receiver for stop action
        myBroadcastReceiver = MyBroadcastReceiver()

        val intentFilter = IntentFilter()
        intentFilter.addAction(STOP_SERVICE_ACTION)
        registerReceiver(this, myBroadcastReceiver,
            intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED)

        // when this activity gets launched, it should start the tracker serivce and bind to it
        // so that it can update the map in here i think
    }

    // notif fcns
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initLocationManager()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null)
            locationManager.removeUpdates(this)
        println("debug: onDestroy called")
        locationManager.removeUpdates(this)
        notificationManager.cancel(NOTIFY_ID)
//        unregisterReceiver(stopReceiver)
    }

    // binder logic
    inner class MyBinder : Binder() {
        fun setmsgHandler(handler: Handler) {
            msgHandler = handler
        }
    }
    override fun onBind(intent: Intent?): IBinder {
        return MyBinder()
    }

    // tracking/map logic ====================================================================================

    // start location manager
    fun initLocationManager() {
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return

            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null)
                onLocationChanged(location)

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

        } catch (e: SecurityException) {
        }
    }



    // fcn to capture location
    override fun onLocationChanged(location: Location) {
        println("debug: onlocationchanged() ${location.latitude} ${location.longitude}")
        val lat = location.latitude
        val lng = location.longitude
//        statsView.text = "Latitude: $lat \nLongitude: $lng"
        val latLng = LatLng(lat, lng)
        val currentPath = pathPoints.value ?: mutableListOf()
        currentPath.add(latLng)
        pathPoints.postValue(currentPath)

        val msg = android.os.Message.obtain()
        msg.what = MSG_INT_VALUE
        val bundle = android.os.Bundle()
        bundle.putParcelable(INT_KEY, latLng)
        msg.data = bundle
        msgHandler?.sendMessage(msg)

        lastLocation?.let {
            totalDistance += it.distanceTo(location)
        }
        lastLocation = location
    }

    private fun buildNotification(): Notification {
        val stopIntent = Intent(STOP_SERVICE_ACTION)
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tracking in Progress")
            .setContentText("Your workout is being recorded.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(R.drawable.ic_launcher_foreground, "Stop", stopPendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Tracking Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            stopSelf()
            notificationManager.cancel(NOTIFY_ID)
            unregisterReceiver(myBroadcastReceiver)
        }
    }
}