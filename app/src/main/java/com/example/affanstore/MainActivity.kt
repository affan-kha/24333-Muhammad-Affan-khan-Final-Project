package com.example.affanstore

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), FragmentChangerListener {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var appSettings: AppSettings
    private var selectedCategory = ""
    private var userId:Int = 0

    companion object{
        lateinit var token:String
        var allProducts = mutableListOf<Product>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appSettings = AppSettings(this)
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"))
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        loadFragment(0,false)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_shop -> {

                    loadFragment(0,true)
                    true
                }
                R.id.menu_cart -> {

                    loadFragment(1,true)
                    true
                }
                R.id.menu_orders -> {
                    loadFragment(2,true)
                    true
                }
                R.id.menu_profile -> {
                    loadFragment(3,true)
                    true
                }
                else -> false
            }
        }

        if (appSettings.getString("TOKEN")!!.isNotEmpty()){
            token = appSettings.getString("TOKEN") as String
        }

        if (appSettings.getInt("USER_ID") != 0){
            userId = appSettings.getInt("USER_ID")
            Log.d("TEST1999","$userId")
        }

        getAllProducts()
    }

    private fun getAllProducts(){
        Utils.startLoading(this)
        val queue = Volley.newRequestQueue(this)
        val url = "https://fakestoreapi.com/products"

        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("TEST199", response.toString())
                Utils.dismiss()
                    //allProducts.addAll(listOf(Gson().fromJson(response.toString(),Product::class.java)))
                 for (i in 0 until response.length()){
                     val obj = response.getJSONObject(i)
                     allProducts.add(Gson().fromJson(obj.toString(),Product::class.java))
                 }
                Log.d("TEST1999", allProducts.toString())
            },
            { error ->
                Utils.dismiss()
                try {
                    Log.d("TEST199", error!!.localizedMessage!!)
                }
                catch (e:java.lang.Exception){
                    e.printStackTrace()
                }

            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun loadFragment(index:Int,flag:Boolean){
        when(index){
            0->{

              if (flag){
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ShopFragment(),"shop")
                    .commit()
              }
              else{
                  supportFragmentManager.beginTransaction().add(R.id.fragment_container,ShopFragment(),"shop")
                      .commit()
              }

            }
            1->{

                if (flag){
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,CartFragment(),"cart")
                        .commit()
                }
                else{
                    supportFragmentManager.beginTransaction().add(R.id.fragment_container,CartFragment(),"cart")
                        .commit()
                }

            }
            2->{

                if (flag){
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,OrdersFragment(),"orders")
                        .commit()
                }
                else{
                    supportFragmentManager.beginTransaction().add(R.id.fragment_container,OrdersFragment(),"orders")
                        .commit()
                }

            }
            3->{

                if (flag){
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ProfileFragment(),"profile")
                        .commit()
                }
                else{
                    supportFragmentManager.beginTransaction().add(R.id.fragment_container,ProfileFragment(),"profile")
                        .commit()
                }

            }
            else->{
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ProductsFragment(selectedCategory),"products")
                    .commit()
            }
        }
    }

    override fun changer(value:String) {
        if (value == "reset"){
            loadFragment(0,true)
            bottomNavigationView.menu.getItem(0).isChecked = true
        }
        else{
            selectedCategory = value
            loadFragment(4,false)
        }

    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentByTag("shop")
        if (currentFragment != null && currentFragment.isVisible){
            finish()
        }
        else{
            loadFragment(0,true)
            bottomNavigationView.menu.getItem(0).isChecked = true
        }

    }

}