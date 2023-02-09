package com.mdev.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {

    lateinit var stopwatch : Chronometer    // The stopwatch
    var running = false                     // Is the stopwatch running?
    var offset: Long = 0                    // The base offset for the stopwatch

    // Key string for use with the Bundle
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(myBundle: Bundle?) {
        super.onCreate(myBundle)
        setContentView(R.layout.activity_main)

        // Get a reference to the stopwatch
        stopwatch = findViewById<Chronometer>(R.id.stopwatch)

        // Restore the previous state
        if(myBundle != null) {
            offset = myBundle.getLong(OFFSET_KEY)
            running = myBundle.getBoolean(RUNNING_KEY)
            if(running) {
                stopwatch.base = myBundle.getLong(BASE_KEY)
                stopwatch.start()
            } else {
                setBaseTime()
            }
        }

        // Define what each button does
        // Start Button - The start button starts the stopwatch if it's not running
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if(!running) {
                setBaseTime()
                stopwatch.start()
                running = true
            }
        }
        // Pause Button - Pauses the stopwatch if it is running
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            if(running) {
                saveOffset()
                stopwatch.stop()
                running = false
            }
        }
        // Reset Button - Sets the offset and stopwatch to 0
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }

    override fun onSaveInstanceState(myBundle: Bundle) {
        // Insert our values into the Bundle to be saved
        myBundle.putLong(OFFSET_KEY, offset)
        myBundle.putBoolean(RUNNING_KEY, running)
        myBundle.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(myBundle)     // When overriding, always call super class
    }

    override fun onPause() {
        super.onPause()
        if(running) {
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if(running) {
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }


    // Update the stopwatch.base time, allowing for any offset
    fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    // Record the offset
    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
}