package com.darkshandev.freshcam.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.activity.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.ActivityMainBinding
import com.darkshandev.freshcam.presentation.classifier.viewmodels.ClassifierViewmodel
import com.darkshandev.freshcam.presentation.fruits.viewmodels.FruitsViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val fruitsViewmodel:FruitsViewmodel by viewModels()
    private val classifierViewmodel: ClassifierViewmodel by viewModels( )
    private  val  binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFragment.navController
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupNavigation() {
        //setup NavigationUi with bottomNavigationView
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController,)
        binding.bottomNavigationView.itemIconTintList = null

    }
}