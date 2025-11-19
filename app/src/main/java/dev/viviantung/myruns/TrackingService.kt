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
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import weka.core.Attribute
import weka.core.Debug
import weka.core.DenseInstance
import weka.core.Instances
import java.io.File
import java.security.KeyStore
import java.text.DecimalFormat
import java.util.ArrayList
import java.util.concurrent.ArrayBlockingQueue

class TrackingService: Service(), LocationListener, SensorEventListener   {
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
    private var totalDistance = 0.0 // in meters
    private var startTime: Long = 0L

    // sensor vars
    private lateinit var mDataset: Instances
    private lateinit var mClassAttribute: Attribute
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var mAccBuffer = ArrayBlockingQueue<Double>(Globals.ACCELEROMETER_BLOCK_CAPACITY)
    private var sensorJob: Job? = null
    private val mFeatLen = Globals.ACCELEROMETER_BLOCK_CAPACITY + 2
    private lateinit var activityType: String


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

        // register accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // Create the container for attributes
        val allAttr = ArrayList<Attribute>()

        // Adding FFT coefficient attributes
        val df = DecimalFormat("0000")
        for (i in 0 until Globals.ACCELEROMETER_BLOCK_CAPACITY) {
            allAttr.add(Attribute(Globals.FEAT_FFT_COEF_LABEL + df.format(i.toLong())))
        }
        // Adding the max feature
        allAttr.add(Attribute(Globals.FEAT_MAX_LABEL))

        // Declare a nominal attribute along with its candidate values
        val labelItems = ArrayList<String>(3)
        labelItems.add(Globals.CLASS_LABEL_STANDING)
        labelItems.add(Globals.CLASS_LABEL_WALKING)
        labelItems.add(Globals.CLASS_LABEL_RUNNING)
        labelItems.add(Globals.CLASS_LABEL_OTHER)
        mClassAttribute = Attribute(Globals.CLASS_LABEL_KEY, labelItems)
        allAttr.add(mClassAttribute)

        // Construct the dataset with the attributes specified as allAttr and
        // capacity 10000
        mDataset = Instances(Globals.FEAT_SET_NAME, allAttr, Globals.FEATURE_SET_CAPACITY)

        // Set the last column/attribute (standing/walking/running) as the class
        // index for classification
        mDataset.setClassIndex(mDataset.numAttributes() - 1)

        // start worker coroutine for activity classification
         startClassificationWorker()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // stop the accerlerometer
        sensorManager.unregisterListener(this)
        sensorJob?.cancel()

        if (locationManager != null)
            locationManager.removeUpdates(this)
        println("debug: onDestroy called")
        locationManager.removeUpdates(this)
        notificationManager.cancel(NOTIFY_ID)
//        unregisterReceiver(stopReceiver)
    }

    // binder logic
    inner class MyBinder : Binder() {
        private var predictedActivityUpdater: ((String) -> Unit)? = null // for activity

        fun setmsgHandler(handler: Handler) {
            msgHandler = handler
        }
        fun getDistance() = totalDistance
        fun getDuration() = (System.currentTimeMillis() - startTime) / 1000.0
        fun getPath() = pathPoints.value ?: mutableListOf()


        fun setPredictedActivityUpdater(updater: (String) -> Unit) {
            predictedActivityUpdater = updater
        }

        fun pushPredictedActivity(label: String) {
            predictedActivityUpdater?.invoke(label)
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

        lastLocation?.let {
            totalDistance += it.distanceTo(location)
        }
        lastLocation = location

        val msg = android.os.Message.obtain()
        msg.what = MSG_INT_VALUE
        val bundle = android.os.Bundle()
        bundle.putParcelable(INT_KEY, latLng)
        msg.data = bundle
        msgHandler?.sendMessage(msg)
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

    // sensor functions
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val mag = kotlin.math.sqrt(
                (event.values[0] * event.values[0] +
                        event.values[1] * event.values[1] +
                        event.values[2] * event.values[2])
            ).toDouble()

            try {
                mAccBuffer.add(mag)
            } catch (e: IllegalStateException) {
                // Buffer full — double its size
                val newBuf = ArrayBlockingQueue<Double>(mAccBuffer.size * 2)
                mAccBuffer.drainTo(newBuf)
                mAccBuffer = newBuf
                mAccBuffer.add(mag)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun startClassificationWorker() {
        sensorJob = CoroutineScope(Dispatchers.Default).launch {

            val inst = DenseInstance(mFeatLen).apply {
                setDataset(mDataset)
            }

            val N = Globals.ACCELEROMETER_BLOCK_CAPACITY
            val accBlock = DoubleArray(N)
            val im = DoubleArray(N)
            val fft = FFT(N)

            var blockSize = 0

            while (isActive) {
                try {
                    val next = mAccBuffer.take()
                    accBlock[blockSize++] = next

                    if (blockSize == N) {
                        blockSize = 0

                        // FFT
                        fft.fft(accBlock, im)

                        for (i in accBlock.indices) {
                            val mag = kotlin.math.sqrt(accBlock[i] * accBlock[i] + im[i] * im[i])
                            inst.setValue(i, mag)
                            im[i] = 0.0
                        }

                        // add max value as feature
                        val maxVal = accBlock.maxOrNull() ?: 0.0
                        inst.setValue(N, maxVal)

                        // convert DenseInstance to Object[] for static classifier
                        val values = Array(mFeatLen) { idx -> inst.value(idx) as Any }

                        // static classification
                        val pIdx = WekaClassifier.classify(values)
                        val label = mDataset.classAttribute().value(pIdx.toInt())

                        // update actvity type
                        activityType = label
                        Log.d("activityTyper","label: $label")
                        (myBinder).pushPredictedActivity(label)

                    }
                } catch (_: Exception) {}
            }
        }
    }



}