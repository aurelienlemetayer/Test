package com.aurelien.test.activities

import android.os.Bundle
import androidx.fragment.app.commit
import com.aurelien.test.R
import com.aurelien.test.core.activities.BaseActivity
import com.aurelien.test.databinding.MainActivityBinding
import com.aurelien.test.fragments.PlacesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityBinding>() {

    private lateinit var placesFragment: PlacesFragment

    override fun getViewBinding() = MainActivityBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        if (savedInstanceState == null) {
            placesFragment = PlacesFragment()
            supportFragmentManager.commit { replace(R.id.frameLayout, placesFragment) }

        } else {
            placesFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                PlacesFragment.TAG
            ) as PlacesFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, PlacesFragment.TAG, placesFragment)
    }
}