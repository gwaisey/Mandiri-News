package dev.rakamin.newsapp.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.rakamin.newsapp.R
import dev.rakamin.newsapp.model.Article

class NewsAdapter(private val articles: MutableList<Article>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADLINE = 0
    private val VIEW_TYPE_LIST = 1

    override fun getItemViewType(position: Int): Int = if (position == 0) VIEW_TYPE_HEADLINE else VIEW_TYPE_LIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADLINE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_headline, parent, false)
            HeadlineViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
            ListViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val article = articles[position]
        if (holder is HeadlineViewHolder) {

            val headlineData = articles.filter { !it.urlToImage.isNullOrEmpty() }.take(5)

            if (headlineData.isNotEmpty()) {
                val carouselAdapter = CarouselAdapter(headlineData)
                holder.viewPager.adapter = carouselAdapter


                holder.viewPager.orientation = androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
            }
        } else if (holder is ListViewHolder) {
            holder.tvTitle.text = article.title
            holder.tvSource.text = article.source.name
            holder.tvDate.text = article.publishedAt
            Glide.with(holder.itemView.context)
                .load(article.urlToImage)
                .error(R.drawable.logo_mandiri)
                .into(holder.imgNews)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(article.url)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articles.size

    class HeadlineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val viewPager: androidx.viewpager2.widget.ViewPager2 = view.findViewById(R.id.viewPagerHeadline)
    }

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSource: TextView = view.findViewById(R.id.tvSource)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val imgNews: ImageView = view.findViewById(R.id.imgNews)
    }
}