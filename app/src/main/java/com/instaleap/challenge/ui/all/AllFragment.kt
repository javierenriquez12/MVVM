package com.instaleap.challenge.ui.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.instaleap.challenge.databinding.FragmentAllBinding
import com.instaleap.challenge.di.Injection
import com.instaleap.challenge.ui.MainViewModel
import com.instaleap.challenge.util.getViewModel
import kotlinx.android.synthetic.main.layout_load_view.view.*

class AllFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var _binding: FragmentAllBinding
    private val binding get() = _binding
    private var listAll: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            mainViewModel = this.getViewModel {
                MainViewModel(Injection().providerResultRepository())
            }
            mainViewModel.results.observe(
                this, {
                    loadAll()
                }
            )
            mainViewModel.isViewLoad.observe(this, Observer(::showLoadOrHide))
        }
    }

    private fun showLoadOrHide(isShowOrHide: Boolean) {
        if (!isShowOrHide) {
            binding.root.lv_service.isVisible = false
            binding.lvAll.isVisible = true
        } else {
            binding.root.lv_service.isVisible = true
            binding.lvAll.isVisible = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAllBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    private fun loadAll() {
        listAll =
            mainViewModel.results.value?.map { it.name.orEmpty() }
        listAll?.let {
            binding.lvAll.apply {
                this.adapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
            }
        }
    }
}
