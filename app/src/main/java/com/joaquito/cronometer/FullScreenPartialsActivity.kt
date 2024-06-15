package com.joaquito.cronometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaquito.cronometer.databinding.ActivityFullScreenPartialsBinding
import com.joaquito.cronometer.fullScreenPartials.PFSAdapter

class FullScreenPartialsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenPartialsBinding

    private lateinit var rvFullScreenPartials: RecyclerView
    private lateinit var PFSAdapter: PFSAdapter

    private val partials = mutableListOf<PartialModel>()

    private var isRunning = false
    private var timerSecs: Int = 0
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            timerSecs++
            val hrs = timerSecs / 3600
            val min = (timerSecs % 3600) / 60
            val secs = timerSecs % 60

            val time = String.format("%02d:%02d:%02d", hrs, min, secs)
            binding.tvTimer.text = time
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenPartialsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        initUI()
    }

    private fun initUI() {
        rvFullScreenPartials = findViewById(R.id.rvFullScreenPartials)
        PFSAdapter = PFSAdapter(partials)
        rvFullScreenPartials.adapter = PFSAdapter
        rvFullScreenPartials.layoutManager = GridLayoutManager(this, 4)
    }

    private fun initListeners() {
        binding.btnStart.setOnClickListener { startTimer() }
        binding.btnStop.setOnClickListener { stopTimer() }
        binding.btnRestart.setOnClickListener { restartTimer() }
        binding.btnPartial.setOnClickListener { takePartial() }

    }

    private fun startTimer() {
        if (!isRunning){
            handler.postDelayed(runnable, 1000)
            isRunning = true
            binding.btnStart.isEnabled = false
            binding.btnStop.isEnabled = true
            binding.btnPartial.isEnabled = true
        }
    }

    private fun stopTimer(){
        if (isRunning){
            handler.removeCallbacks(runnable)
            isRunning = false
            binding.btnStart.isEnabled = true
            binding.btnStart.text = "RESUME"
            binding.btnStop.isEnabled = false
            binding.btnRestart.isEnabled = true
            binding.btnPartial.isEnabled = false

        }
    }

    private fun restartTimer(){
        handler.removeCallbacks(runnable)
        timerSecs = 0
        isRunning = false
        binding.tvTimer.text = "00:00:00"
        binding.btnStart.isEnabled = true
        binding.btnStop.isEnabled = false
        binding.btnRestart.isEnabled = false
        binding.btnStart.text = "START"
        binding.btnPartial.isEnabled = false
        partials.clear()


    }

    private fun takePartial(){
        val hrs = timerSecs / 3600
        val min = (timerSecs % 3600) / 60
        val secs = timerSecs % 60

        val time = String.format("%02d:%02d:%02d", hrs, min, secs)
        if (isRunning){
            partials.add(PartialModel(time))
            rvFullScreenPartials.isVisible = true
            PFSAdapter.notifyDataSetChanged()

        }


    }

}