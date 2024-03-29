package com.example.ecomap.HomeMaps
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ecomap.R
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader


data class WaterData(val name: String, val status: String, val drinkable: String, val type: String, val long: String, val lat: String)

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var city: Array<String>? = null
    private lateinit var googleMap: GoogleMap

    private lateinit var display: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        city = arguments?.getStringArray(ARG_CITY)

        display = view.findViewById(R.id.water_stats)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap?) {
        val zoomLevel = 10f

        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_styles)
        gMap?.setMapStyle(mapStyleOptions)

        try {
            val inputStream = requireContext().assets.open("bloom-report.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Read all lines and join them into a single string
            val csvContent = reader.use { it.readText() }

            // Use the CsvReader to parse the CSV content
            val csvReader = CsvReader()
            val rows: List<List<String>> = csvReader.readAll(csvContent)

            val markers: MutableList<Marker> = mutableListOf()
            val waterList: MutableList<WaterData> = mutableListOf()

            gMap?.let { googleMap ->
                this.googleMap = googleMap
                city?.let { city ->

                    for (location in rows) {
                        if (location[12] != "Bloom Longitude") {
                            val california =
                                LatLng(location[11].toDouble(), location[12].toDouble())

                            val newWater = WaterData(
                                location[6],
                                location[17],
                                location[25],
                                location[29],
                                location[12],
                                location[11]
                            )

                            waterList.add(newWater)


                            // New size for the bitmap
                            val newWidth = 300 // New width of the bitmap
                            val newHeight = 300 // New height of the bitmap

                            // Load the bitmap from the resource
                            val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.ecomaps_logo)

                            // Resize the bitmap to the new dimensions
                            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false)

                            // Convert the resized bitmap to a BitmapDescriptor
                            val icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap)

                            // Load the bitmap from the resource
                            val originalBitmap2 = BitmapFactory.decodeResource(resources, R.drawable.ecomaps_logo2)

                            // Resize the bitmap to the new dimensions
                            val resizedBitmap2 = Bitmap.createScaledBitmap(originalBitmap2, newWidth, newHeight, false)

                            // Convert the resized bitmap to a BitmapDescriptor
                            val icon2 = BitmapDescriptorFactory.fromBitmap(resizedBitmap2)



                            Log.d("Coords", location[12]+" "+location[11])
                            val marker = googleMap.addMarker(
                                MarkerOptions()
                                    .position(california)
                                    .title("${newWater.name}")
                                    .icon(icon)
                            )
                            markers.add(marker)

                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    california,
                                    zoomLevel
                                )
                            )

                            // Set up marker click listener
                            googleMap.setOnMarkerClickListener { clickedMarker ->
                                val index = markers.indexOf(clickedMarker)
                                    // Inflate the other layout containing the TextView
                                    display.visibility = View.VISIBLE
                                    display.text = "${waterList[index].name}\nType: ${waterList[index].type}\nIssue: ${waterList[index].status}\nDrinkable: ${waterList[index].drinkable}\nLong: ${waterList[index].long}\nLat: ${waterList[index].lat}"

                                clickedMarker.setIcon(icon2)
                                true // Return true to consume the event
                            }

                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MapsFragment", "Error reading CSV file: ${e.message}")
        }
    }

    companion object {
        private const val ARG_CITY = "coords"

        fun newInstance(city: Array<String>): MapsFragment {
            val fragment = MapsFragment()
            val args = Bundle()
            args.putStringArray(ARG_CITY, city)
            fragment.arguments = args
            return fragment
        }
    }
}
