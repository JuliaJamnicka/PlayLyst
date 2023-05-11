package cz.muni.fi.pv239.juliajamnicka.playlyst

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.ActivityMainBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.api.SessionManager

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        SessionManager.setup(applicationContext)

        setSupportActionBar(binding.appbar)

        // this worked before i added the appbarconfig, now the back buttons' black AGAIN
        // TODO: fix backButton color
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_appbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.show()


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.graph = navController.navInflater.inflate(R.navigation.nav_graph).apply {
            setStartDestination( if (SessionManager.isUserAuthorized())
                R.id.playlistsFragment else
                    R.id.spotifyAuthorizationFragment
            )
        }

        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.playlistsFragment,
                R.id.moodsFragment
            )
        )
        binding.appbar.setupWithNavController(navController, appBarConfig)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigation.visibility = visibility
    }

}