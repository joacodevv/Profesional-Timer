package com.joaquito.cronometer.fullScreenPartials

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaquito.cronometer.PartialModel
import com.joaquito.cronometer.R
import com.joaquito.cronometer.partials.PartialViewHolder

class PFSAdapter(var partials: List<PartialModel>): RecyclerView.Adapter<PFSViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PFSViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_partial,parent, false)
        return PFSViewHolder(view)
    }

    override fun getItemCount() = partials.size

    override fun onBindViewHolder(holder: PFSViewHolder, position: Int) {
        holder.render(partials[position])
    }
}