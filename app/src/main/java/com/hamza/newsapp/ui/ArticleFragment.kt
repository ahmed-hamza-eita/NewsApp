package com.hamza.newsapp.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hamza.newsapp.R
import com.hamza.newsapp.databinding.FragmentArticleBinding
import com.hamza.newsapp.utils.BaseFragment


class ArticleFragment : BaseFragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    //  private var totalAvailablePages = 1
    //   private val list = ArrayList<TVShowModel.TvShow>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_article, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentArticleBinding.bind(view)


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}