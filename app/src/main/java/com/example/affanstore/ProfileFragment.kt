package com.example.affanstore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var profileImage:CircleImageView
    private lateinit var nameView:MaterialTextView
    private lateinit var userNameView:MaterialTextView
    private lateinit var userEmailView:MaterialTextView
    private lateinit var userPhoneView:MaterialTextView
    private lateinit var userAddressView:MaterialTextView
    private lateinit var logoutBtn:MaterialButton
    private lateinit var aboutBtn:MaterialButton
    private lateinit var appSettings: AppSettings
    private var userId:Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appSettings = AppSettings(activity!!)
        if (appSettings.getInt("USER_ID") != 0){
            userId = appSettings.getInt("USER_ID")
            Log.d("TEST1999","$userId")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_profile, container, false)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        profileImage = v.findViewById(R.id.profile_image)
        nameView = v.findViewById(R.id.user_full_name)
        userNameView = v.findViewById(R.id.user_name)
        userEmailView = v.findViewById(R.id.user_email)
        userPhoneView = v.findViewById(R.id.user_phone)
        userAddressView = v.findViewById(R.id.user_address)
        logoutBtn = v.findViewById(R.id.profile_logout_btn)
        aboutBtn = v.findViewById(R.id.about_app)
        logoutBtn.setOnClickListener {
            appSettings.clear()
            val intent = Intent(activity!!,StartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            activity!!.startActivity(intent)
        }

        aboutBtn.setOnClickListener {

            val intent = Intent(activity!!,AboutActivity::class.java)
            activity!!.startActivity(intent)
        }
        return v
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    private fun getUserDetails(){
        Utils.startLoading(activity!!)
        val queue = Volley.newRequestQueue(activity)
        val url = "https://fakestoreapi.com/users/$userId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Utils.dismiss()
                Log.d("TEST199", response.toString())
                val user = Gson().fromJson(response.toString(),User::class.java)
                Glide
                    .with(activity!!)
                    .load("https://images.squarespace-cdn.com/content/v1/6213c340453c3f502425776e/1669043491508-5WM3GPI5DCO6HJ5HT3VN/ai-feature_dezeen_1704_col_7.png?format=500w")
                    .into(profileImage)

                nameView.text = "${user.name.firstname} ${user.name.lastname}"
                userNameView.text = user.username
                userEmailView.text = user.email
                userPhoneView.text = user.phone
                userAddressView.text = user.address.toString()
                val loc = LatLng(user.address.geolocation.lat.toDouble(), user.address.geolocation.long.toDouble())
                mMap.addMarker(
                    MarkerOptions()
                        .position(loc)
                        .title("Marker"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))

            },
            { error ->
                Utils.dismiss()
                try {
                    Log.d("TEST199", error!!.localizedMessage!!)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        )

        queue.add(jsonObjectRequest)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(
//            MarkerOptions()
//            .position(sydney)
//            .title("Marker"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

}