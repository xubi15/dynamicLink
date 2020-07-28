package com.zubi.dynamiclink.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zubi.dynamiclink.model.CampaignData
import com.zubi.dynamiclink.R
import kotlinx.android.synthetic.main.item_row.view.*

class CampaignAdapter(
    private val campaignList: MutableList<CampaignData>,
    private val context: Context
) :
    RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        return CampaignViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_row, parent, false)
        )
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return campaignList.size
    }

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        holder.itemView.title.text = campaignList[position].itemName
        holder.itemView.description.text = campaignList[position].itemDescription
        Glide.with(context).load(campaignList[position].imageUrl).into(holder.itemView.imageView)

    }

    class CampaignViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}