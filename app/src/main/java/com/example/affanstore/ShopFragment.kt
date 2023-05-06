package com.example.affanstore

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley


class ShopFragment : Fragment(),ShopCategoriesAdapter.OnItemClickListener {
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: ShopCategoriesAdapter
    private var categoriesList = mutableListOf<String>()
    private var fragmentChangerListener:FragmentChangerListener?=null
    private var token:String?=null
    private lateinit var appSettings: AppSettings
    override fun onAttach(context: Context) {
        super.onAttach(context)
        appSettings = AppSettings(activity!!)
        token = appSettings.getString("TOKEN")
        fragmentChangerListener = activity as FragmentChangerListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_shop, container, false)

        recyclerview = v.findViewById(R.id.shop_categories_recyclerview)

        recyclerview.layoutManager = GridLayoutManager(activity,2)
        adapter = ShopCategoriesAdapter(activity!!,categoriesList as ArrayList<String>)
        recyclerview.adapter = adapter
        adapter.setOnItemClickListener(this)
        return v
    }

    override fun onResume() {
        super.onResume()
        if (categoriesList.isEmpty()){
            getShopCategories()
        }
    }

    private fun getShopCategories(){
//        Utils.startLoading(activity!!)
        val queue = Volley.newRequestQueue(activity)
        val url = "https://fakestoreapi.com/products/categories"

        val jsonObjectRequest = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
//                Utils.dismiss()
                Log.d("TEST199", response.toString())

                for (i in 0 until response.length()){
                    categoriesList.add(response.get(i).toString())
                }
               if (categoriesList.size > 0){
                   adapter.notifyItemChanged(0,categoriesList.size)
               }
            },
            { error ->
//                Utils.dismiss()
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
        val item = categoriesList[position]
        fragmentChangerListener!!.changer(item)
    }

}