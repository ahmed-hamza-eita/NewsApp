package com.hamza.newsapp.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(Const.SERIALIZABLE_KEY, it)

            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle)
        }


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
                      binding.rvSearchNews.addOnScrollListener(this@SearchNewsFragment.scrollListener)
                    }
                }

                is Resource.Error -> {
                    ProgressLoading.dismiss()
                    showToast(response.message)
                }

            }
        })
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotBegining = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Const.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotBegining && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.searchNews(binding.etSearch.text.toString())
                isScrolling = false
            } else {
                binding.rvSearchNews.setPadding(0, 0, 0, 0)
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}