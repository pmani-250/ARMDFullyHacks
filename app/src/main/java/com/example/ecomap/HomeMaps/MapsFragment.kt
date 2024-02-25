package com.example.ecomap.HomeMaps
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecomap.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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
        gMap?.let { googleMap ->
            this.googleMap = googleMap
            city?.let { city ->
                val california = LatLng(city[1].toDouble(), city[0].toDouble())
                googleMap.addMarker(MarkerOptions().position(california).title("Marker in $california"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(california))
            }
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
