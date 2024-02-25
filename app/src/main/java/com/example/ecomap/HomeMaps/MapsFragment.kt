package com.example.ecomap.HomeMaps
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecomap.R
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader


data class WaterData(val long: String, val lat: String, val status: String)

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var city: Array<String>? = null
    private lateinit var googleMap: GoogleMap

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

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap?) {
        val zoomLevel = 10f

        try {
            val inputStream = requireContext().assets.open("bloom-report.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Read all lines and join them into a single string
            val csvContent = reader.use { it.readText() }

            // Use the CsvReader to parse the CSV content
            val csvReader = CsvReader()
            val rows: List<List<String>> = csvReader.readAll(csvContent)

            gMap?.let { googleMap ->
                this.googleMap = googleMap
                city?.let { city ->

                    for (location in rows) {
                        if (location[12] != "Bloom Longitude") {
                            val california =
                                LatLng(location[11].toDouble(), location[12].toDouble())
                            val status = location[17]
                            Log.d("Coords", location[12]+" "+location[11])
                            googleMap.addMarker(
                                MarkerOptions().position(california).title("$status")
                            )
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    california,
                                    zoomLevel
                                )
                            )
                        }
                    }

//                    if (city.size >= 2) {
//                        val california = LatLng(city[1].toDouble(), city[0].toDouble())
//                        googleMap.addMarker(MarkerOptions().position(california).title("Marker in $california"))
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(california, zoomLevel))
//                    } else {
//                        Log.e("MapsFragment", "City coordinates are invalid.")
//                    }
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
