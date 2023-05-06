package com.example.affanstore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson


class OrdersFragment : Fragment(),OrdersAdapter.OnItemClickListener {
    private var ordersList = mutableListOf<Product>()
    private lateinit var appSettings: AppSettings
    private var userId:Int?=null
    private var token:String?=null
    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var adapter:OrdersAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appSettings = AppSettings(activity!!)
        userId = appSettings.getInt("USER_ID")
        token = appSettings.getString("TOKEN")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_orders, container, false)
        ordersRecyclerView = v.findViewById(R.id.orders_recyclerview)
        ordersRecyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = OrdersAdapter(activity!!, ordersList as ArrayList<Product>)
        ordersRecyclerView.adapter = adapter
        adapter.setOnClickListener(this)
        return v
    }

    override fun onResume() {
        super.onResume()
        if (ordersList.isEmpty()){
            getOrdersList()
        }
    }

    private fun getOrdersList(){
        Utils.startLoading(activity!!)
        val queue = Volley.newRequestQueue(activity)
        val url = "https://fakestoreapi.com/carts/user/$userId?startdate=2019-12-10&enddate=2020-10-10"

        val jsonObjectRequest = object: JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                Utils.dismiss()
                Log.d("TEST199", response.toString())
                for (j in 0 until response.length()) {
                    val cartItem = response.getJSONObject(j)
                    val cart = Gson().fromJson(cartItem.toString(),Cart::class.java)
                    for (i in 0 until cart.products.size){
                        val prd = cart.products[i]
                        val product:Product? = MainActivity.allProducts.find { it.id == prd.productId }
                        product!!.quantity = prd.quantity
                        ordersList.add(product)
                    }
                }


                if (ordersList.size > 0){

                    adapter.notifyItemChanged(0,ordersList.size)

                }
                else{
                    adapter.notifyDataSetChanged()
                }
            },
            { error ->
                Utils.dismiss()
                try {
                    Log.d("TEST199", error!!.localizedMessage!!)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headerMap: MutableMap<String, String> = HashMap()
//                headerMap["Content-Type"] = "application/json"
                headerMap["Authorization"] = "Bearer $token"
                return headerMap
            }
        }

        queue.add(jsonObjectRequest)
    }

    override fun onItemClick(position: Int) {
        val item = ordersList[position]
        startActivity(Intent(activity,ProductDetailActivity::class.java).apply {
            putExtra("PRODUCT",item)
        })
    }
}