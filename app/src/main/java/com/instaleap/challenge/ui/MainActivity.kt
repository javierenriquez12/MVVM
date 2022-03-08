package com.instaleap.challenge.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.instaleap.challenge.R
import com.instaleap.challenge.databinding.ActivityMainBinding
import com.instaleap.challenge.di.Injection
import com.instaleap.challenge.ui.all.AllFragment
import com.instaleap.challenge.ui.movies.MoviesFragment
import com.instaleap.challenge.ui.series.SeriesFragment
import com.instaleap.challenge.util.getViewModel


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        getViewModel {
            MainViewModel(Injection().providerResultRepository())
        }
    }
    private lateinit var binding: ActivityMainBinding
    private val allFragment: Fragment = AllFragment()
    private val moviesFragment: Fragment = MoviesFragment()
    private val seriesFragment: Fragment = SeriesFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = allFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setInitTitleActionBar()
        setContentView(binding.root)
        loadResults()
        binding.navView.setOnItemSelectedListener {
            navigateBottomBar(it)
        }
        loadFragmentOptionsNavigation()
    }

    private fun setInitTitleActionBar() {
        supportActionBar?.title = getString(
            R.string.title_movies_series,
            getString(R.string.title_all)
        )
    }

    private fun navigateBottomBar(menuItem: MenuItem) =
        when (menuItem.itemId) {
            R.id.navigation_all -> {
                fm.beginTransaction().hide(active).show(allFragment).commit()
                active = allFragment
                supportActionBar?.title = getString(
                    R.string.title_movies_series,
                    getString(R.string.title_all)
                )
                true
            }
            R.id.navigation_movies -> {
                fm.beginTransaction().hide(active).show(moviesFragment).commit()
                active = moviesFragment
                supportActionBar?.title = getString(R.string.title_movies)
                true
            }
            R.id.navigation_series -> {
                fm.beginTransaction().hide(active).show(seriesFragment).commit()
                active = seriesFragment
                supportActionBar?.title = getString(R.string.title_series)
                true
            }
            else -> false
        }


    private fun loadFragmentOptionsNavigation() {
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_activity_main, seriesFragment).hide(seriesFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_activity_main, moviesFragment).hide(moviesFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_activity_main, allFragment).commit()
    }

    private fun loadResults() {
        mainViewModel.loadResults()
    }
}