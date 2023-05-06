package com.example.affanstore

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class ProductDetailActivity : AppCompatActivity() {

    private var product:Product?=null
    private lateinit var titleView:MaterialTextView
    private lateinit var productImage:AppCompatImageView
    private lateinit var descView:MaterialTextView
    private lateinit var toolbar: Toolbar
    private lateinit var buyBtn:MaterialButton
    private lateinit var incrementBtn:AppCompatImageView
    private lateinit var decrementBtn:AppCompatImageView
    private lateinit var quantityTextView:MaterialTextView
    private var quantity:Int = 1
    private var userId:Int?=null
    private lateinit var appSettings: AppSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        titleView = findViewById(R.id.product_title_view)
        productImage = findViewById(R.id.product_image_view)
        descView = findViewById(R.id.product_desc_view)
        toolbar = findViewById(R.id.pd_toolbar)
        buyBtn = findViewById(R.id.buy_btn)
        incrementBtn = findViewById(R.id.increment_image)
        decrementBtn = findViewById(R.id.decrement_image)
        quantityTextView = findViewById(R.id.quantity_view)
        appSettings = AppSettings(this);
        userId = appSettings.getInt("USER_ID")
        setSupportActionBar(toolbar)
        if (intent != null && intent.hasExtra("PRODUCT")){
            product = intent.getSerializable("PRODUCT",Product::class.java)

            titleView.text = product!!.title
            Glide
                .with(this)
                .load(product!!.image)
                .into(productImage)
            descView.text = product!!.description

            supportActionBar!!.title = product!!.title
            toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
           supportActionBar!!.setDisplayHomeAsUpEnabled(true)
           supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow_back_48)
        }

        incrementBtn.setOnClickListener {
            quantity += 1
            quantityTextView.text = "$quantity"
        }

        decrementBtn.setOnClickListener {
             if (quantity != 1){
                 quantity -= 1
                 quantityTextView.text = "$quantity"
             }
        }

        buyBtn.setOnClickListener {
             Utils.startLoading(this)
             val productsList = mutableListOf<CartProduct>()
             productsList.add(CartProduct(product!!.id,quantity))
              val cartItem = Cart(userId!!,getDateFromTimeStamp(System.currentTimeMillis()),productsList)
            val queue = Volley.newRequestQueue(this)
            val url = "https://fakestoreapi.com/carts"

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, JSONObject(Gson().toJson(cartItem,Cart::class.java)),
                { response ->
                     Utils.dismiss()
                    Log.d("TEST199", response.toString())
                    Toast.makeText(this,"Item added into cart successfully!",Toast.LENGTH_SHORT).show()
                },
                { error ->
                    Utils.dismiss()
                    //Log.d("TEST199", error!!.localizedMessage!!)
                    try {
                        Toast.makeText(this, error.localizedMessage!!,Toast.LENGTH_SHORT).show()
                    }
                    catch (e:java.lang.Exception){
                        e.printStackTrace()
                    }
                }
            )

            queue.add(jsonObjectRequest)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun <T : Serializable?> Intent.getSerializable(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializableExtra(key, m_class)!!
        else
            this.getSerializableExtra(key) as T
    }

    private fun getDateFromTimeStamp(miliseconds:Long):String{
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(miliseconds)
    }
}