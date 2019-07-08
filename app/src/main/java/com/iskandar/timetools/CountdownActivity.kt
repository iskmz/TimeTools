package com.iskandar.timetools

import android.app.Service
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.EditText
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.activity_countdown.*

class CountdownActivity : AppCompatActivity() {


    val HOURS_MAX_VALUE = 99

    val HOURS = "h"
    val MINUTES = "m"
    val SECONDS = "s"

    val context = this@CountdownActivity as Context

    lateinit var timeCountdown:TimeCountdown
    lateinit var countdownService: CountdownService
    var countdownOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        initialize()
    }

    private fun initialize() {
        timeCountdown = TimeCountdown()
        countdownService = CountdownService(timeCountdown)
        setNumberPickers()
        setButtons()
    }



    private fun setNumberPickers() {
        // min , max values //
        pickerHours.minValue = 0
        pickerHours.maxValue = HOURS_MAX_VALUE
        pickerMinutes.minValue = 0
        pickerMinutes.maxValue = 59
        pickerSeconds.minValue = 0
        pickerSeconds.maxValue = 59

    }

    private fun setButtons() {
        btnCountdownReset.setOnClickListener {
            txtCountdown.text = getString(R.string.countdown_placeholder)
        }

        btnCountdownStart.setOnClickListener {
            if(countdownOn){
                stopService(Intent(context,CountdownService::class.java))
                countdownOn=false
            }
            else{
                // START / RESUME
                intent = Intent(context,CountdownService::class.java)
                intent.putExtra(HOURS,timeCountdown.hour)
                intent.putExtra(MINUTES,timeCountdown.min)
                intent.putExtra(SECONDS,timeCountdown.sec)
                startService(Intent(context,CountdownService::class.java))
                countdownOn=true
            }

        }
    }
}


class TimeCountdown(var sec: Int, var min: Int, var hour: Int) {

    constructor() : this(0, 0, 0)
    constructor(other: TimeDisplay) : this(other.sec, other.min, other.hour)

    fun tickDown() {
        this.sec -= 1 // tick tock tick tock tick tock
        if (this.sec < 0) {
            this.sec = 59
            this.min -= 1
            if (this.min < 0) {
                this.min = 59
                this.hour -= 1
            }
        }
    }

    override fun toString(): String {
        return (if (hour < 10) "0" else "") + hour + ":" +
                (if (min < 10) "0" else "") + min + ":" +
                (if (sec < 10) "0" else "") + sec
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeDisplay

        if (sec != other.sec) return false
        if (min != other.min) return false
        if (hour != other.hour) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sec
        result = 31 * result + min
        result = 31 * result + hour
        return result
    }


}

class CountdownService(val startTime:TimeCountdown) : Service() {
    var currentTime:TimeCountdown = startTime


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
