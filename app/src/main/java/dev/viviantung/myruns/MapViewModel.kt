package dev.viviantung.myruns

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Looper
import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import android.os.Handler


class MapViewModel :  ViewModel(), ServiceConnection {
    private var myMessageHandler: MyMessageHandler
    private val _mapper = MutableLiveData<LatLng>()
    val mapper: LiveData<LatLng>
        get() {
            return _mapper
        }

    init {
        myMessageHandler = MyMessageHandler(Looper.getMainLooper())
    }

    override fun onServiceConnected(name: ComponentName, iBinder: IBinder) {
        println("debug: ViewModel: onServiceConnected() called; ComponentName: $name")
        val tempBinder = iBinder as TrackingService.MyBinder
        tempBinder.setmsgHandler(myMessageHandler)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        println("debug: Activity: onServiceDisconnected() called~~~")
    }

    inner class MyMessageHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            if (msg.what == TrackingService.MSG_INT_VALUE) {
                val bundle = msg.data
                val latLng = bundle.getParcelable<LatLng>(TrackingService.INT_KEY)
                latLng?.let {
                    _mapper.value = it
                }
            }
        }
    }
}