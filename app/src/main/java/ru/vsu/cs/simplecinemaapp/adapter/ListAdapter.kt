package ru.vsu.cs.simplecinemaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.simplecinemaapp.GlideApp
import ru.vsu.cs.simplecinemaapp.R
import ru.vsu.cs.simplecinemaapp.holders.FilmDataHolder
import ru.vsu.cs.simplecinemaapp.model.Film

class ListAdapter(
    private var films: List<Film>,
    private val context: Context,
    private val clickListener: ClickListener,
    private val holdListener: HoldListener
) : RecyclerView.Adapter<ListAdapter.LogHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return LogHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LogHolder, position: Int) {
        val currentFilm = films[position]
        holder.description.text = currentFilm.nameRu

        val imageView = holder.image
        GlideApp.with(context)
            .load(currentFilm.posterUrl)
            .into(imageView)

        holder.additionText.text =
            "${currentFilm.genres?.get(0)?.genre.toString()} (${currentFilm.year} г.)"

        // click to card
        holder.cardView.setOnClickListener { clickListener.onItemClick(films[position]) }
        // hold card
        holder.cardView.setOnLongClickListener {
            holdListener.onItemHold(films[position], position)
            true
        }

        // Проверяем находится ли данный фильм в избранном
        val favList = FilmDataHolder.favouriteFilmList.value?.toList() ?: emptyList()
        if (favList.contains(currentFilm)) {
            holder.favouriteIcon.visibility = View.VISIBLE
        } else {
            holder.favouriteIcon.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int {
        return films.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLogs(films: List<Film>) {
        this.films = ArrayList(films)
        notifyDataSetChanged()
    }

    inner class LogHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.filmCardName)
        val image: ImageView = itemView.findViewById(R.id.listImage)
        val additionText: TextView = itemView.findViewById(R.id.genreCardName)
        val cardView: CardView = itemView.findViewById(R.id.filmCard)
        val favouriteIcon: ImageView = itemView.findViewById(R.id.cardFavouriteIcon)
    }
}
