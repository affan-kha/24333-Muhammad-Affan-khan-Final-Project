package com.example.affanstore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView


class ProductsAdapter(
    private val context: Context,
    private var productsList: ArrayList<Product>
) : RecyclerView.Adapter<ProductsAdapter.ItemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    open class ItemViewHolder(itemView:View, private val mListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val textView: MaterialTextView = itemView.findViewById(R.id.product_name_view)
        val image:AppCompatImageView = itemView.findViewById(R.id.product_image_view)
        fun dataBinding(item: Product, position: Int,context: Context) {
            Glide
                .with(context)
                .load(item.image)
                .into(image)
            textView.text = item.title
            itemView.setOnClickListener {
                mListener.onItemClick(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item_row, parent, false)

        return ItemViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val product = productsList[position]
         holder.dataBinding(product,position,context)

    }

    override fun getItemCount(): Int {
        return productsList.size
    }

}