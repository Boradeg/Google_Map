package com.example.googlemap

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.example.googlemap.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // Toast.makeText(, "d", Toast.LENGTH_SHORT).show()
      //  CT.success2(this, "Success");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
        //getCurrentLocations()

    }

    private fun getCurrentLocations()
    {
       if(checkPermissions())
       {
           if(locationEnableds())
           {
               //final latitude and longitude
               if (ActivityCompat.checkSelfPermission(
                       this,
                       Manifest.permission.ACCESS_FINE_LOCATION
                   ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                       this,
                       Manifest.permission.ACCESS_COARSE_LOCATION
                   ) != PackageManager.PERMISSION_GRANTED
               ) {
                   // TODO: Consider calling
                   //    ActivityCompat#requestPermissions
                   // here to request the missing permissions, and then overriding
                   //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                   //                                          int[] grantResults)
                   // to handle the case where the user grants the permission. See the documentation
                   // for ActivityCompat#requestPermissions for more details.
                   return
               }
               fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task->
                   val location:Location?=task.result
                   if(location==null)
                   {
                       Toast.makeText(this, "null location", Toast.LENGTH_SHORT).show()
                   }
                   else{
                       Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
//                       binding.lati.text= location.latitude.toString()
//                       binding.longi.text= location.longitude.toString()
                   }

               }
           }
           else
           {
               //setting open here
               Toast.makeText(this, "turn on location", Toast.LENGTH_SHORT).show()
               startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
           }
       }
        else
       {
           //req permission here
           reqPermission()
       }


    }

    private fun reqPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION
        ,android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    companion object{
    private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
}
    private fun checkPermissions(): Boolean {
     if(ActivityCompat.checkSelfPermission(this,
             android.Manifest.permission.ACCESS_COARSE_LOCATION)
         ==PackageManager.PERMISSION_GRANTED
         &&
         ActivityCompat.checkSelfPermission(this,
             android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
     {
         return true
     }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
                getCurrentLocations()
            }
            else
            {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun locationEnableds(): Boolean {
      val locationManager:LocationManager=getSystemService(Context.LOCATION_SERVICE)as LocationManager
     // val locationManager:LocationManager=getSystemService(LOCATION_SERVICE)as LocationManager
      return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}