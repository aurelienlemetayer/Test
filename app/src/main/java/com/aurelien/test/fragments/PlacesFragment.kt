package com.aurelien.test.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aurelien.test.R
import com.aurelien.test.adapters.PlacesRecyclerViewAdapter
import com.aurelien.test.core.fragments.BaseFragment
import com.aurelien.test.core.utils.setGone
import com.aurelien.test.core.utils.setVisible
import com.aurelien.test.core.utils.snack
import com.aurelien.test.databinding.PlacesFragmentBinding
import com.aurelien.test.services.EventObserver
import com.aurelien.test.services.models.Place
import com.aurelien.test.viewmodels.PlacesViewModel


class PlacesFragment : BaseFragment<PlacesFragmentBinding>(),
    PlacesRecyclerViewAdapter.PlacesRecyclerViewAdapterCallback {

    companion object {
        const val TAG = "PlacesFragment"
    }

    private val placesRecyclerViewAdapter =
        PlacesRecyclerViewAdapter(mutableListOf(), this)

    private val viewModel: PlacesViewModel by activityViewModels()

    override fun getViewBinding() = PlacesFragmentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLiveData()
        initSearchViewEditText()
        initRecyclerView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState()
        super.onSaveInstanceState(outState)
    }

    private fun initSearchViewEditText() {
        binding.searchViewEditText.apply {
            doAfterTextChanged {
                if (it.isNullOrEmpty()) {
                    setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_search_hint,
                        0,
                        0,
                        0
                    )
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_search,
                        0,
                        0,
                        0
                    )
                }
            }

            setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchPlaces(text.toString())
                    return@OnEditorActionListener true
                }
                false
            })
        }
    }

    private fun initRecyclerView() {
        with(binding.recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = placesRecyclerViewAdapter
        }
    }

    private fun initLiveData() {
        viewModel.contentVisibilityLiveData.observe(
            viewLifecycleOwner,
            { setContentVisibility(it) })
        viewModel.loaderVisibilityLiveData.observe(viewLifecycleOwner, { setLoaderVisibility(it) })

        viewModel.placesLiveData.observe(viewLifecycleOwner, { it?.let { setPlaces(it) } })

        viewModel.showSearchingPlacesErrorLiveData.observe(
            viewLifecycleOwner,
            EventObserver { if (it) showSearchingPlacesErrorLiveData() })
    }


    private fun setLoaderVisibility(visible: Boolean) {
        binding.loader.apply {
            if (visible) {
                setVisible()
            } else {
                setGone()
            }
        }
    }

    private fun setContentVisibility(visible: Boolean) {
        binding.recyclerView.apply {
            if (visible) {
                setVisible()
            } else {
                setGone()
            }
        }
    }

    private fun setPlaces(places: List<Place>) {
        placesRecyclerViewAdapter.setPlaces(places)
    }

    private fun showSearchingPlacesErrorLiveData() {
        binding.root.snack(R.string.common_error_message)
    }

    override fun placeClicked(place: Place) {
        //TODO
    }
}