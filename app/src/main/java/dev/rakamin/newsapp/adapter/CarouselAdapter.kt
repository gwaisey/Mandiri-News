package dev.rakamin.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.rakamin.newsapp.R
import dev.rakamin.newsapp.model.Article

class CarouselAdapter(private val carouselItems: List<Article>) :
    RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCarousel: ImageView = view.findViewById(R.id.imgNews)
        val tvTitle: TextView = view.findViewById(R.id.tvCarouselTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = carouselItems[position]


        holder.tvTitle.text = article.title

        Glide.with(holder.itemView.context)
            .load(article.urlToImage)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.logo_mandiri)
            .into(holder.imgCarousel)
    }

    override fun getItemCount(): Int = carouselItems.size
}