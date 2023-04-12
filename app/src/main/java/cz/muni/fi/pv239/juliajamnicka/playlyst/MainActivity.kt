package cz.muni.fi.pv239.juliajamnicka.playlyst

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.graph

        if (true) { // check if authorization code present
            navGraph.setStartDestination(R.id.spotifyAuthorizationFragment)
        } else { // check validity of access token in the background, refresh it if necessary
            navGraph.setStartDestination(R.id.playlistsFragment)
        }

        binding.bottomNavigation.setupWithNavController(navController)
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigation.visibility = visibility
    }
}