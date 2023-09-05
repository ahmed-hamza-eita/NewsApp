package com.hamza.newsapp.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hamza.newsapp.utils.BaseFragment
import com.hamza.newsapp.R
import com.hamza.newsapp.adapters.NewsAdapter
import com.hamza.newsapp.databinding.FragmentBreakingNewsBinding
import com.hamza.newsapp.utils.ProgressLoading
import com.hamza.newsapp.utils.Resource
import com.hamza.newsapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : BaseFragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()
    private val newsAdapter=NewsAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_breaking_news, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBreakingNewsBinding.bind(view)

       // setupNewsAdapter()

        observer()

    }

    private fun observer() {
        viewModel.breakingNewsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                     ProgressLoading.dismiss()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                        binding.rvBreakingNews.adapter=newsAdapter
                    }
                }

                is Resource.Error -> {
                    ProgressLoading.dismiss()
                    response.message?.let {
                        showToast(it)

                    }
                }
                is Resource.Loading ->{
                    ProgressLoading.show(requireActivity())
                }


            }

        })
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}