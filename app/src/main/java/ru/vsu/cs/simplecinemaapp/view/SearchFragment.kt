package ru.vsu.cs.simplecinemaapp.view

import MainViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.simplecinemaapp.R
import ru.vsu.cs.simplecinemaapp.adapter.ClickListener
import ru.vsu.cs.simplecinemaapp.adapter.HoldListener
import ru.vsu.cs.simplecinemaapp.adapter.ListAdapter
import ru.vsu.cs.simplecinemaapp.holders.FilmDataHolder
import ru.vsu.cs.simplecinemaapp.model.Film

class SearchFragment(private val aLoadingDialog: ALoadingDialog) : Fragment(R.layout.fragment_search) {

    private var page = 1
    private var pageCount: Int? = 0
    private val foundFilmList = ArrayList<Film>()
    private val filmId = FilmDataHolder.filmOverviewId
    private val favList = FilmDataHolder.favouriteFilmList

    private lateinit var viewModel: MainViewModel
    private lateinit var button: Button
    private lateinit var text: EditText
    private lateinit var adapter: ListAdapter
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        recyclerView = view.findViewById(R.id.filmListView)
        text = view.findViewById(R.id.search_string)
        button = view.findViewById(R.id.search_button)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        adapter = ListAdapter(foundFilmList, requireContext(), clickListener, holdListener)

        favList.observe(viewLifecycleOwner){
            adapter.notifyDataSetChanged()
        }
        searchButton()
        setupRecyclerView()


        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                aLoadingDialog.show()
            } else {
                aLoadingDialog.cancel()
            }
        }
        viewModel.foundFilmData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                pageCount = response.pagesCount
                response.films?.let {
                    recyclerView.visibility = View.VISIBLE
                    val beforeSize = foundFilmList.size
                    foundFilmList.addAll(it)
                    adapter.notifyItemRangeInserted(beforeSize, foundFilmList.size - beforeSize)
                }
            } else {
                Toast.makeText(requireContext(), "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchButton() {
        button.setOnClickListener {
            foundFilmList.clear()
            val keyword = text.text.toString()
            if (keyword.isNotEmpty()) {
                viewModel.findByKeyword(keyword, 1)
                hideKeyboard()
            } else {
                Toast.makeText(requireContext(), "Введите ключевое слово", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter
        // Настройка RecyclerView, чтобы при конце прокрутки доставились новые фильмы
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && page <= pageCount!!) {
                    page++
                    viewModel.findByKeyword(text.text.toString(), page)
                }
            }
        })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private val clickListener = object : ClickListener {
        override fun onItemClick(film: Film) {
            filmId.postValue(film.filmId!!)
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changePageToFilmOverview()
            println("Clicked on film ${film.nameRu}")
        }
    }

    private val holdListener = object : HoldListener {
        override fun onItemHold(film: Film, position: Int) {
            val currentList = favList.value?.toMutableList() ?: mutableListOf()
            val toast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
            if (!currentList.contains(film)) {
                currentList.add(film)
                favList.postValue(currentList)
                toast.setText("Фильм ${film.nameRu} был добавлен в избранное")
                toast.show()
                adapter.notifyItemChanged(position)
            } else {
                toast.setText("Фильм ${film.nameRu} уже в избранном")
                toast.show()
            }
            println("HOLD on film ${film.nameRu}")
        }
    }
}
