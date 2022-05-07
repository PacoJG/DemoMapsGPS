package com.dev.eraydel.demomapsclase

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    // maps es el objeto que representa la clase de GoogleMap
    private lateinit var map:GoogleMap

    //configurar el mapa, se manda llamar OnMapReadyCallBack e implementar miembros onMapReady

    // para ubicar al usuario
    companion object {
        const val REQUEST_CODE_LOCATION=0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createFragment()
    }

    private fun enableLocation(){
        if(!::map.isInitialized)return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled=true
        } else {
            requestLocationPermission()
        }
    }

    private fun createFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //este codigo se ejecutara cuando el fragment termine de cargarse
        map = googleMap
        crearMrker(19.52829336148813,-99.18654127621309, "Punta Palermo")
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)


        //tratar de acceder a la ubicación del gps
        enableLocation()
        findViewById<Button>(R.id.btnRest1).setOnClickListener{
            Toast.makeText(this,"Esto es el restaurante 1", Toast.LENGTH_SHORT).show()
            crearMrker(19.502882697137235, -99.12847994923067,"Italiannis")
        }

    }

    private fun crearMrker(lat: Double, lon: Double, txtUbicacion: String){
        val coordenadas = LatLng(lat,lon )
        val marker = MarkerOptions().position(coordenadas).title(txtUbicacion)
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordenadas,18f),
            4000,null
        )

    }

    //para determinar si el usuario ya ha garantizado el permiso de ubicación
    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    //para solicitar al usuario el permiso de ubicación
    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            //mostrar la ventana de permiso
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled=true
            } else {
                Toast.makeText(this,"Para activar permisos, ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()
            }

            else -> {}
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Esta es mi ubicación", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this,"latitud ${p0.latitude} and longitud ${p0.longitude} ", Toast.LENGTH_SHORT).show()
    }

}




//Step 1: Configure Google API KEY
//Step 2: Permissions on Manifest File (add both)
    // ACCESS_COARSE_LOCATION de forma general - menos precisión
    // FINE_LOCATION: mayor precisión
//paso 3: Agregar dependencias com.google.android.gms:play-services-maps:18.0.2
//paso 4: manifest agregar metadata com.google.android.geo.API_KEY