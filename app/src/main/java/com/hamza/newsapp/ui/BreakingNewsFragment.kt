package com.hamza.newsapp.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamza.newsapp.utils.BaseFragment
import com.hamza.newsapp.R
import com.hamza.newsapp.adapters.NewsAdapter
import com.hamza.newsapp.databinding.FragmentBreakingNewsBinding
import com.hamza.newsapp.utils.Const
import com.hamza.newsapp.utils.ProgressLoading
import com.hamza.newsapp.utils.Resource
import com.hamza.newsapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : BaseFragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()
    private val newsAdapter = NewsAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_breaking_news, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBreakingNewsBinding.bind(view)

        actions()
        observer()


    }

    private fun actions() {
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(Const.SERIALIZABLE_KEY, it)
            }
            navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle)


        }
    }


    private fun observer() {
        viewModel.breakingNewsLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressLoading.dismiss()
                    isScrolling = false
                    response.data?.let {

                        newsAdapter.differ.submitList(it.articles.toList())
                        binding.apply {
                            rvBreakingNews.adapter = newsAdapter
                            rvBreakingNews.addOnScrollListener(this@BreakingNewsFragment.scrollListener)
                        }
                        val totalPages = (it.totalResults) / (Const.QUERY_PAGE_SIZE) + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                    }
                }

                is Resource.Error -> {
                    ProgressLoading.dismiss()
                    isScrolling = false
                    response.message?.let {
                        showToast(it)

                    }
                }

                is Resource.Loading -> {
                    ProgressLoading.show(requireActivity())
                    isScrolling = true
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
                viewModel.getBreakingNews("eg")
                isScrolling = false
            } else {
                binding.rvBreakingNews.setPadding(0, 0, 0, 0)
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