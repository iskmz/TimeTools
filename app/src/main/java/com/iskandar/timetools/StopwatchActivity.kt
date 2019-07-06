package com.iskandar.timetools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class StopwatchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stopwatch)
    }
}




open class TimeDisplay(var sec: Int, var min: Int, var hour: Int) {

    constructor() : this(0, 0, 0)
    constructor(other: TimeDisplay) : this(other.sec, other.min, other.hour)

    open fun tick() {
        this.sec += 1 // tick tock tick tock tick tock
        if (this.sec > 59) {
            this.sec = 0
            this.min += 1
            if (this.min > 59) {
                this.min = 0
                this.hour += 1
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


class TimeDisplayMSec : TimeDisplay {

    private var msec: Int = 0
    private var tickValue: Int = 0

    constructor(tickValue: Int) : super() {
        this.msec = 0
        this.tickValue = tickValue
    }

    constructor(other: TimeDisplayMSec) : super(other){
        this.msec = other.msec
        this.tickValue = other.tickValue
    }

    // i.e. static members //
    companion object {
        val MSEC_FULL_TICK = 1000
        val MSEC_HALF_TICK = 500
        val MSEC_QUARTER_TICK = 250
        val MSEC_FIFTH_TICK = 200
        val MSEC_EIGHTH_TICK = 125
        val MSEC_TENTH_TICK = 100
    }


    override fun tick() {
        msec += tickValue
        if (msec > 999) {
            msec = 0
            sec += 1
            if (sec > 59) {
                sec = 0
                min += 1
                if (min > 59) {
                    min = 0
                    hour += 1
                }
            }
        }
    }

    override fun toString(): String {
        return super.toString() +
                "." + (if (msec < 100) if (msec < 10) "00" else "0" else "") + msec
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as TimeDisplayMSec

        if (msec != other.msec) return false
        if (tickValue != other.tickValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + msec
        result = 31 * result + tickValue
        return result
    }
}
