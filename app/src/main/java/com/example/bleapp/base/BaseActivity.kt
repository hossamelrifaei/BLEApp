package com.example.bleapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bleapp.ui.Router
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var router: Router


    abstract fun layoutRes(): Int
    abstract fun initialScreen(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
        router.onCreate(this, initialScreen())
    }

    override fun onBackPressed() {
        if (!router.pop()) {
            super.onBackPressed()
        }
    }


}