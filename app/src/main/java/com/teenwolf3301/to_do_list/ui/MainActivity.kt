package com.teenwolf3301.to_do_list.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teenwolf3301.to_do_list.databinding.ActivityMainBinding
import com.teenwolf3301.to_do_list.util.APP_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        APP_ACTIVITY = this
    }
}