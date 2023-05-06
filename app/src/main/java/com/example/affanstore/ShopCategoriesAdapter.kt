package com.example.affanstore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView


class ShopCategoriesAdapter(
    private val context: Context,
    private var categoriesList: ArrayList<String>
) : RecyclerView.Adapter<ShopCategoriesAdapter.ItemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    open class ItemViewHolder(itemView:View, private val mListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val textView: MaterialTextView = itemView.findViewById(R.id.shop_category_name_view)
        fun dataBinding(item: String, position: Int) {
            textView.text = item
            itemView.setOnClickListener {
                mListener.onItemClick(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_category_item_row, parent, false)

        return ItemViewHolder(view, mListener!!)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val category = categoriesList[position]
         holder.dataBinding(category,position)

    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

}