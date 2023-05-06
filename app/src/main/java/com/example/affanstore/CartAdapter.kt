package com.example.affanstore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView

class CartAdapter(val context: Context, val cartList: ArrayList<Product>) :
    RecyclerView.Adapter<CartAdapter.ItemViewHolder>() {

    private var listener: OnItemClickListener? = null
    private var subTotal = 0

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
//        val itemAddons: MaterialTextView
        val itemSubTotal: MaterialTextView
//        val itemDeleteImage: AppCompatImageView
        val itemIncrementBtn: AppCompatImageView
        val itemDecrementBtn: AppCompatImageView
        val itemQuantityView: MaterialTextView


        init {
            itemImage = itemView.findViewById(R.id.item_cart_image)
            itemTitle = itemView.findViewById(R.id.item_cart_title)
            itemQuantityWithPrice = itemView.findViewById(R.id.item_cart_price_quantity)
            itemSubTotal = itemView.findViewById(R.id.cart_item_sub_total)
//            itemDeleteImage = itemView.findViewById(R.id.cart_item_cancel_image)
            itemIncrementBtn = itemView.findViewById(R.id.item_cart_increment_btn)
            itemDecrementBtn = itemView.findViewById(R.id.item_cart_decrement_btn)
            itemQuantityView = itemView.findViewById(R.id.item_cart_quantity_view)

            itemView.setOnClickListener {
                mListener.onItemClick(layoutPosition)
            }

//            itemIncrementBtn.setOnClickListener {
//                mListener.onItemCounter(layoutPosition,itemQuantityView,"increment")
//            }
//
//            itemDecrementBtn.setOnClickListener {
//                mListener.onItemCounter(layoutPosition,itemQuantityView,"decrement")
//            }

//            itemDeleteImage.setOnClickListener {
//                mListener.onItemDelete(layoutPosition)
//            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_row, parent, false)
        return ItemViewHolder(v, listener!!)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = cartList[position]

        Glide
            .with(context)
            .load(item.image)
            .into(holder.itemImage)
          holder.itemTitle.text = item.title
        holder.itemQuantityWithPrice.text = "$ ${item.price} X ${item.quantity}"
        subTotal = (item.price.toFloat() * item.quantity).toInt()
//
        holder.itemQuantityView.text = "${item.quantity}"
//        if (item.addons!!.isNotEmpty())
//        {
//            holder.itemAddons.visibility = View.VISIBLE
//            val commaSeperatedString = item.addons!!.joinToString {
//                it.addon_name
//            }
//
//            holder.itemAddons.text = commaSeperatedString
//            plusAddonTotal(item.quantity,item.addons!!)
//        }
//        else
//        {
//            holder.itemAddons.visibility = View.GONE
//        }

        holder.itemSubTotal.text = "$subTotal"
    }


    override fun getItemCount(): Int {
        return cartList.size
    }

}