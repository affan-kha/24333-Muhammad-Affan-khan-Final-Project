package com.example.affanstore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView

class OrdersAdapter(val context: Context, val ordersList: ArrayList<Product>) :
    RecyclerView.Adapter<OrdersAdapter.ItemViewHolder>() {

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(mListener: OnItemClickListener) {
        listener = mListener
    }

    class ItemViewHolder(itemView: View, mListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val itemImage: AppCompatImageView
        val itemTitle: MaterialTextView
        val itemQuantityWithPrice: MaterialTextView


        init {
            itemImage = itemView.findViewById(R.id.item_cart_image)
            itemTitle = itemView.findViewById(R.id.item_cart_title)
            itemQuantityWithPrice = itemView.findViewById(R.id.item_cart_price_quantity)

            itemView.setOnClickListener {
                mListener.onItemClick(layoutPosition)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order_history_row, parent, false)
        return ItemViewHolder(v, listener!!)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = ordersList[position]

        Glide
            .with(context)
            .load(item.image)
            .into(holder.itemImage)
          holder.itemTitle.text = item.title
        holder.itemQuantityWithPrice.text = "Quantity: ${item.quantity}"


    }


    override fun getItemCount(): Int {
        return ordersList.size
    }

}