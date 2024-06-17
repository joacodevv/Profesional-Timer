package com.joaquito.cronometer.splits.fullScreenPartials

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joaquito.cronometer.PartialModel
import com.joaquito.cronometer.R

class PFSViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvPartial: TextView = view.findViewById(R.id.tvPartial)

    fun render(partial: PartialModel) {
        tvPartial.text = partial.partial
    }
}