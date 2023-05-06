package com.example.affanstore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson


class ProductsFragment(private val selectedCate:String) : Fragment(),ProductsAdapter.OnItemClickListener {

    private val _selectedCate = selectedCate
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: ProductsAdapter
    private var productList = mutableListOf<Product>()
    private var token:String?=null
    private lateinit var appSettings: AppSettings

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appSettings = AppSettings(activity!!)
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
        val v = inflater.inflate(R.layout.fragment_products, container, false)
        recyclerview = v.findViewById(R.id.products_recyclerview)

        recyclerview.layoutManager = GridLayoutManager(activity,2)
        adapter = ProductsAdapter(activity!!,productList as ArrayList<Product>)
        recyclerview.adapter = adapter
        adapter.setOnItemClickListener(this)
        return v
    }

    override fun onResume() {
        super.onResume()
        if (productList.isEmpty()){
            getProducts()
        }
    }

    private fun getProducts(){
        Utils.startLoading(activity!!)
        val queue = Volley.newRequestQueue(activity)
        val url = "https://fakestoreapi.com/products/category/$_selectedCate"
        val gson = Gson()
        val jsonObjectRequest = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                Utils.dismiss()
                Log.d("TEST199", response.toString())

                for (i in 0 until response.length()){
                    productList.add(gson.fromJson(response.getJSONObject(i).toString(),Product::class.java))
                }
                if (productList.size > 0){
                    adapter.notifyItemChanged(0,productList.size)
                }
            },
            { error ->
                Utils.dismiss()
                try {
                    Log.d("TEST199", error!!.localizedMessage!!)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headerMap: MutableMap<String, String> = HashMap()
                headerMap["Authorization"] = "Bearer $token"
                return headerMap
            }
        }

        queue.add(jsonObjectRequest)
    }

    override fun onItemClick(position: Int) {
        val item = productList[position]
        startActivity(Intent(activity,ProductDetailActivity::class.java).apply {
            putExtra("PRODUCT",item)
        })
    }

}