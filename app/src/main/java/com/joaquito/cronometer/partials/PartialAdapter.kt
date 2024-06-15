package com.joaquito.cronometer.partials

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joaquito.cronometer.PartialModel
import com.joaquito.cronometer.R

class PartialAdapter(var partials: List<PartialModel>): RecyclerView.Adapter<PartialViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_partial,parent, false)
        return PartialViewHolder(view)
    }

    override fun getItemCount() = partials.size

    override fun onBindViewHolder(holder: PartialViewHolder, position: Int) {
        holder.render(partials[position])

    }

}