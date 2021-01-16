package com.aurelien.test.activities

import com.aurelien.test.core.activities.BaseActivity
import com.aurelien.test.databinding.MainActivityBinding

class MainActivity : BaseActivity<MainActivityBinding>() {

    override fun getViewBinding() = MainActivityBinding.inflate(layoutInflater)
}