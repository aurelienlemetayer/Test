package com.aurelien.test.activities

import android.os.Bundle
import androidx.fragment.app.commit
import com.aurelien.test.R
import com.aurelien.test.core.activities.BaseActivity
import com.aurelien.test.databinding.DeparturesActivityBinding
import com.aurelien.test.fragments.DeparturesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeparturesActivity : BaseActivity<DeparturesActivityBinding>() {

    companion object {
        private const val TAG = "DeparturesActivity"
        const val PLACE_ID_ARGUMENT_KEY = "${TAG}_PLACE_ID_ARGUMENT_KEY"
        const val PLACE_NAME_ARGUMENT_KEY = "${TAG}_PLACE_NAME_ARGUMENT_KEY"
    }

    private lateinit var departuresFragment: DeparturesFragment

    override fun getViewBinding() = DeparturesActivityBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        binding.backButton.setOnClickListener { onBackPressed() }
        binding.title.text = intent.getStringExtra(PLACE_NAME_ARGUMENT_KEY)

        if (savedInstanceState == null) {
            departuresFragment = DeparturesFragment()
            val bundle = Bundle()
            bundle.putString(
                DeparturesFragment.PLACE_ID_ARGUMENT_KEY,
                intent.getStringExtra(PLACE_ID_ARGUMENT_KEY)
            )
            departuresFragment.arguments = bundle
            supportFragmentManager.commit { replace(R.id.frameLayout, departuresFragment) }
        } else {
            departuresFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                DeparturesFragment.TAG
            ) as DeparturesFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, DeparturesFragment.TAG, departuresFragment)
    }
}