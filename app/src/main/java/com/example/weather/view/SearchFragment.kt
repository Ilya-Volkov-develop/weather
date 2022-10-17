package com.example.weather.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentSearchBinding
import com.example.weather.model.CityDTO
import com.example.weather.model.Coords
import com.example.weather.recycleradapter.SearchFragmentAdapter
import com.example.weather.utils.BUNDLE_KEY_SEARCH_IN_HOME
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


class SearchFragment:Fragment(),OnItemClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val formList = ArrayList<CityDTO>()
    private val adapter: SearchFragmentAdapter by lazy { SearchFragmentAdapter(this) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {
        binding.btnBack.setOnClickListener { requireActivity().onBackPressed() }
        getAllCities()
        initSearch()
        binding.citiesRecyclerView.adapter = adapter
        adapter.setCities(formList)
    }

    private fun getAllCities() {
        try {
            val obj = JSONObject(loadJSONFromAsset())
            val citiesArray = obj.getJSONArray("cities")

            for (i in 0 until citiesArray.length()) {
                val item = citiesArray.getJSONObject(i)
                val coords = item.getJSONObject("coords")
                val lat = coords.getString("lat")
                val lon = coords.getString("lon")
                val district = item.getString("district")
                val name = item.getString("name")
                val population = item.getString("population")
                val subject = item.getString("subject")

                //Add your values in your `ArrayList` as below:
                formList.add(CityDTO(Coords(lat, lon),district,name,population,subject))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    private fun loadJSONFromAsset(): String {
        var json: String? = null
        try {
            val inputStream: InputStream = requireActivity().assets.open("russian-cities.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return json!!
    }

    private fun initSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onItemClick(cityDTO: CityDTO) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container,HomeFragment.newInstance(Bundle().apply {
                putParcelable(BUNDLE_KEY_SEARCH_IN_HOME,cityDTO)
            })).commit()
    }
}