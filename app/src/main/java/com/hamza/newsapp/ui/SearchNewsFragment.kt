package com.hamza.newsapp.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hamza.newsapp.utils.BaseFragment
import com.hamza.newsapp.R
import com.hamza.newsapp.adapters.NewsAdapter
import com.hamza.newsapp.databinding.FragmentSearchNewsBinding
import com.hamza.newsapp.utils.Const
import com.hamza.newsapp.utils.ProgressLoading
import com.hamza.newsapp.utils.Resource
import com.hamza.newsapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : BaseFragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    private val adapter = NewsAdapter()

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search_news, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchNewsBinding.bind(view)

        actions()
        observer()


    }

    private fun actions() {
//        adapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable(Const.SERIALIZABLE_KEY, it)
//
//            }
//            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle)
//        }


        binding.etSearch.addTextChangedListener { editable ->
            job?.hashCode()
            job = MainScope().launch {
                delay(Const.SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }


    }

    private fun observer() {
        viewModel.searchNewsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    ProgressLoading.show(requireActivity())
                }

                is Resource.Success -> {
                    ProgressLoading.dismiss()
                    response.data?.let {
                        adapter.differ.submitList(it.articles)
                        binding.rvSearchNews.adapter = adapter
                    }
                }

                is Resource.Error -> {
                    ProgressLoading.dismiss()
                    showToast(response.message)
                }

            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}