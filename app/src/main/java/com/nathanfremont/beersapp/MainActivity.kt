package com.nathanfremont.beersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nathanfremont.beersapp.beerlist.BeerListScreen
import com.nathanfremont.beersapp.ui.theme.BeersAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeersAppTheme {
                BeerListScreen()
            }
        }
    }
}