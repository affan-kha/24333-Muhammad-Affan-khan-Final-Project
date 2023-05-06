package com.example.affanstore

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson


class CartFragment : Fragment(),CartAdapter.OnItemClickListener {

    private var cartList = mutableListOf<Product>()
    private lateinit var appSettings: AppSettings
    private var userId:Int?=null
    private var token:String?=null
    private lateinit var adapter: CartAdapter
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartTotalView:MaterialTextView
    private var total: Float = 0F
    private lateinit var bottomCartDetail:LinearLayout
    private lateinit var checkoutBtn:MaterialButton
    private var fragmentChangerListener:FragmentChangerListener?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentChangerListener = activity as FragmentChangerListener
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
        val v = inflater.inflate(R.layout.fragment_cart, container, false)
       cartTotalView = v.findViewById(R.id.cart_total)
        bottomCartDetail = v.findViewById(R.id.bottom_cart_detail)
       cartRecyclerView = v.findViewById(R.id.cart_recyclerview)
        checkoutBtn = v.findViewById(R.id.checkout_btn)
        cartRecyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = CartAdapter(activity!!, cartList as ArrayList<Product>)
        cartRecyclerView.adapter = adapter
        adapter.setOnClickListener(this)


        checkoutBtn.setOnClickListener {
            Utils.startLoading(activity!!)
            Handler(Looper.getMainLooper()).postDelayed({
                Utils.dismiss()
              Toast.makeText(activity!!,"Order has been placed successfully!",Toast.LENGTH_SHORT).show()
              generateNotification("New Order","Affan Store got a new order!")
                fragmentChangerListener!!.changer("reset")
            },5000)
        }

        return v
    }

    override fun onResume() {
        super.onResume()
        if (cartList.isEmpty()){
            getCartList()
        }
    }

    private fun getCartList(){
        Utils.startLoading(activity!!)
        val queue = Volley.newRequestQueue(activity)
        val url = "https://fakestoreapi.com/carts/user/$userId?startdate=2019-12-10&enddate=2020-10-10"

        val jsonObjectRequest = object: JsonArrayRequest(
            Method.GET, url, null,
            { response ->
                Utils.dismiss()
                Log.d("TEST199", response.toString())
                for (j in 0 until response.length()){
                    val cartItem = response.getJSONObject(j)
                    val cart = Gson().fromJson(cartItem.toString(),Cart::class.java)
                    for (i in 0 until cart.products.size){
                        val prd = cart.products[i]
                        val product:Product? = MainActivity.allProducts.find { it.id == prd.productId }
                        product!!.quantity = prd.quantity
                        cartList.add(product)
                    }
                }

              if (cartList.size > 0){
                  bottomCartDetail.visibility = View.VISIBLE
                adapter.notifyItemChanged(0,cartList.size)
                  calculateTotal(cartList as ArrayList<Product>)
              }
                else{
                  bottomCartDetail.visibility = View.GONE
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
                headerMap["Authorization"] = "Bearer $token"
                return headerMap
            }
        }

        queue.add(jsonObjectRequest)
    }

    private fun calculateTotal(cartList: ArrayList<Product>) {
        total = 0F
        for (item: Product in cartList) {
            total += (item.price.toFloat() * item.quantity)
        }

        cartTotalView.text = "$$total"
        Log.d("TEST199", total.toString())


    }

    override fun onItemClick(position: Int) {

    }


    private fun generateNotification(title:String,desc:String){
        val builder = NotificationCompat.Builder(activity!!, "10001")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(desc)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(activity!!,0,Intent(activity,MainActivity::class.java),0 or PendingIntent.FLAG_MUTABLE))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationId = 1 // A unique ID for the notification
        val notificationManager =
            activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

}