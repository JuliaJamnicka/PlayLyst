package cz.muni.fi.pv239.juliajamnicka.playlyst

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
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

        setSupportActionBar(binding.appbar)

        // this worked before i added the appbarconfig, now the back buttons' black AGAIN
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_appbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.show()


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.graph

        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.playlistsFragment,
                R.id.moodsFragment
            )
        )
        binding.appbar.setupWithNavController(navController, appBarConfig)
        binding.bottomNavigation.setupWithNavController(navController)

        if (false) { // check if authorization code present
            // aaand this is ignored, great
            navGraph.setStartDestination(R.id.spotifyAuthorizationFragment)
        } else {
            navGraph.setStartDestination(R.id.playlistsFragment)
        }
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigation.visibility = visibility
    }
}