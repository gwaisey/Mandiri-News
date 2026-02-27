package dev.rakamin.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.rakamin.newsapp.adapter.NewsAdapter
import dev.rakamin.newsapp.databinding.ActivityMainBinding
import dev.rakamin.newsapp.model.Article
import dev.rakamin.newsapp.network.NewsInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val articles = mutableListOf<Article>()
    private val newsAdapter = NewsAdapter(articles)
    private var currentPage = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchNews("indonesia", 1)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvAllNews.layoutManager = layoutManager
        binding.rvAllNews.adapter = newsAdapter
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text.toString()
                if (query.isNotEmpty()) {
                    currentPage = 1
                    fetchNews(query, currentPage)
                }
                true
            } else {
                false
            }
        }

        binding.rvAllNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        currentPage++
                        fetchNews("indonesia", currentPage)
                    }
                }
            }
        })
    }

    private fun fetchNews(query: String, page: Int) {
        isLoading = true
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NewsInterface::class.java)

        lifecycleScope.launch {
            try {
                val response = apiService.getAllNews(query, page)

                withContext(Dispatchers.Main) {
                    if (page == 1) {
                        articles.clear()

                        val newsWithImage = response.articles.filter { !it.urlToImage.isNullOrEmpty() }
                        val newsWithoutImage = response.articles.filter { it.urlToImage.isNullOrEmpty() }


                        articles.addAll(newsWithImage)
                        articles.addAll(newsWithoutImage)
                    } else {
                        articles.addAll(response.articles)
                    }
                    newsAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}