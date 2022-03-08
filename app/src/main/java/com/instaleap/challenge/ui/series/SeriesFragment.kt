package com.instaleap.challenge.ui.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.instaleap.challenge.databinding.FragmentSeriesBinding
import com.instaleap.challenge.di.Injection
import com.instaleap.challenge.ui.MainViewModel
import com.instaleap.challenge.util.getViewModel
import kotlinx.android.synthetic.main.layout_load_view.view.*

class SeriesFragment : Fragment() {

    companion object {
        private const val RESULT_FILTER_TYPE_SERIES = "SERIES"
    }

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentSeriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = requireActivity().getViewModel {
            MainViewModel(Injection().providerResultRepository())
        }
        mainViewModel.results.observe(
            requireActivity(), {
                loadSeries()
            }
        )
        mainViewModel.isViewLoad.observe(this, Observer(::showLoadOrHide))
    }

    private fun showLoadOrHide(isShowOrHide: Boolean) {
        if (!isShowOrHide) {
            binding.root.lv_service.isVisible = false
            binding.lvSeries.isVisible = true
        } else {
            binding.root.lv_service.isVisible = true
            binding.lvSeries.isVisible = false
        }
    }

    private fun loadSeries() {
        val listSeries =
            mainViewModel.results.value?.filter { it.type == RESULT_FILTER_TYPE_SERIES }
                ?.map { it.name.orEmpty() }
        listSeries?.let {
            binding.lvSeries.apply {
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
        _binding = FragmentSeriesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}