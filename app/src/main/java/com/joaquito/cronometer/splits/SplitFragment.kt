package com.joaquito.cronometer.splits

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaquito.cronometer.PartialModel
import com.joaquito.cronometer.R
import com.joaquito.cronometer.databinding.FragmentSplitBinding
import com.joaquito.cronometer.databinding.FragmentStopWatchBinding
import com.joaquito.cronometer.splits.fullScreenPartials.PFSAdapter


class SplitFragment : Fragment() {

    private var _binding: FragmentSplitBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initUI()
    }

    private fun initUI() {
        rvFullScreenPartials = binding.rvFullScreenPartials
        PFSAdapter = PFSAdapter(partials)
        rvFullScreenPartials.adapter = PFSAdapter
        rvFullScreenPartials.layoutManager = GridLayoutManager(context, 4)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSplitBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}