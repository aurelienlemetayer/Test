package com.aurelien.test.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aurelien.test.R
import com.aurelien.test.adapters.DeparturesRecyclerViewAdapter
import com.aurelien.test.core.fragments.BaseFragment
import com.aurelien.test.core.utils.setGone
import com.aurelien.test.core.utils.setVisible
import com.aurelien.test.core.utils.snack
import com.aurelien.test.data.models.Departure
import com.aurelien.test.databinding.DeparturesFragmentBinding
import com.aurelien.test.services.EventObserver
import com.aurelien.test.viewmodels.DeparturesViewModel


class DeparturesFragment : BaseFragment<DeparturesFragmentBinding>() {

    companion object {
        const val TAG = "DeparturesFragment"
        const val PLACE_ID_ARGUMENT_KEY = "${TAG}_PLACE_ID_ARGUMENT_KEY"
    }

    private val placesRecyclerViewAdapter = DeparturesRecyclerViewAdapter(mutableListOf())

    private val viewModel: DeparturesViewModel by activityViewModels()

    override fun getViewBinding() = DeparturesFragmentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeId = arguments?.getString(PLACE_ID_ARGUMENT_KEY)
            ?: throw NullPointerException("The placeId must not be null")

        initLiveData()
        initRecyclerView()

        if (savedInstanceState == null) {
            viewModel.loadNextDepartures(placeId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState()
        super.onSaveInstanceState(outState)
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

        viewModel.departuresLiveData.observe(viewLifecycleOwner, { it?.let { setDepartures(it) } })

        viewModel.showGettingNextDeparturesErrorLiveData.observe(
            viewLifecycleOwner,
            EventObserver { if (it) showGettingNextDeparturesErrorLiveData() })
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

    private fun setDepartures(departures: List<Departure>) {
        placesRecyclerViewAdapter.setDepartures(departures.toMutableList())
    }

    private fun showGettingNextDeparturesErrorLiveData() {
        binding.root.snack(R.string.common_error_message)
    }
}