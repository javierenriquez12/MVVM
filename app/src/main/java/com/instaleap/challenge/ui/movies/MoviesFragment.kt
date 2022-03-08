package com.instaleap.challenge.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.instaleap.challenge.databinding.FragmentMoviesBinding
import com.instaleap.challenge.di.Injection
import com.instaleap.challenge.ui.MainViewModel
import com.instaleap.challenge.util.getViewModel
import kotlinx.android.synthetic.main.layout_load_view.view.*

class MoviesFragment : Fragment() {

    companion object {
        private const val RESULT_FILTER_TYPE_MOVIE = "MOVIE"
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var _binding: FragmentMoviesBinding
    private val binding: FragmentMoviesBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            mainViewModel = this.getViewModel {
                MainViewModel(Injection().providerResultRepository())
            }
            mainViewModel.results.observe(
                this, {
                    loadMovies()
                }
            )
            mainViewModel.isViewLoad.observe(this, Observer(::showLoadOrHide))
        }
    }

    private fun showLoadOrHide(isShowOrHide: Boolean) {
        if (!isShowOrHide) {
            binding.root.lv_service.isVisible = false
            binding.lvMovies.isVisible = true
        } else {
            binding.root.lv_service.isVisible = true
            binding.lvMovies.isVisible = false
        }
    }

    private fun loadMovies() {
        val listMovies =
            mainViewModel.results.value?.filter { it.type == RESULT_FILTER_TYPE_MOVIE }
                ?.map { it.name.orEmpty() }
        listMovies?.let {
            binding.lvMovies.apply {
                this.adapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
}