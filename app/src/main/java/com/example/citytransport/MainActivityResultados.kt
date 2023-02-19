package com.example.citytransport

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.app_bar_main.*
import org.json.JSONObject

class MainActivityResultados: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback {

    //NAVIGATION DRAWER
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    //Google Maps
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_resultados)

        //Activity de Boletos
        detalles_boleto.setOnClickListener{
            startActivity(Intent(this, MainActivityBoleto::class.java))
        }

        //NAVIGATION DRAWER
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        title = null
        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView:NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Initialize Google Maps and its callbacks
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Fondo de los iconos
        val btnBus: BottomNavigationView = findViewById(R.id.bottom_navigation_bus)
        btnBus.itemIconTintList = null

        val btnOpcions: BottomNavigationView = findViewById(R.id.bottom_navigation_opcions)
        btnOpcions.itemIconTintList = null

        //Menu desplegable - panel selector
        val languages1 = resources.getStringArray(R.array.destino)
        val layout1 = ArrayAdapter(this, R.layout.list_item, languages1)
        val autocomplete1 = findViewById<AutoCompleteTextView>(R.id.destino)
        autocomplete1.setAdapter(layout1)

        val languages2 = resources.getStringArray(R.array.Ubicación)
        val layout2 = ArrayAdapter(this, R.layout.list_item, languages2)
        val autocomplete2= findViewById<AutoCompleteTextView>(R.id.ubicacion)
        autocomplete2.setAdapter(layout2)


    }


    //SIRVE PARA INBOCAR FUNCIONES ENTRE ACTIVITY
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.promociones -> Toast.makeText(this, "Promociones", Toast.LENGTH_SHORT).show()
            R.id.ayuda -> Toast.makeText(this, "Ayuda", Toast.LENGTH_SHORT).show()
            R.id.configuracion -> Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show()
            R.id.cerrar -> Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()


        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //Funciones de los mapas de google maps
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        // Sample coordinates
        val latLngOrigin = LatLng(20.222404486910534, -89.30624495833517) // Mi ubicacion
        val latLngIntermedio = LatLng(20.660085679561327, -89.46368765133299)
        val latLngDestination = LatLng(20.96493306256816, -89.63037414287166) // Donde voy
        this.googleMap!!.addMarker(MarkerOptions().position(latLngOrigin).title("Tekax"))
        this.googleMap!!.addMarker(MarkerOptions().position(latLngDestination).title("Mérida"))
        this.googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom( latLngIntermedio,8.6f))
        val path: MutableList<List<LatLng>> = ArrayList()
        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=20.222404486910534, -89.30624495833517&destination=20.96493306256816, -89.63037414287166&key=AIzaSyDFWoHTjlJu56uaMzB2Mcr0ROWaGrt3h5E"
        val directionsRequest = object : StringRequest(Request.Method.GET, urlDirections, Response.Listener<String> {
                response ->
            val jsonResponse = JSONObject(response)
            // Get routes
            val routes = jsonResponse.getJSONArray("routes")
            val legs = routes.getJSONObject(0).getJSONArray("legs")
            val steps = legs.getJSONObject(0).getJSONArray("steps")
            for (i in 0 until steps.length()) {
                val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                path.add(PolyUtil.decode(points))
            }
            for (i in 0 until path.size) {
                this.googleMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
            }
        }, Response.ErrorListener {

        }){}
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)
    }
}