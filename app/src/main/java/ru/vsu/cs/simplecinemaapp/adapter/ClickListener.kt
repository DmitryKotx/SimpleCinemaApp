package ru.vsu.cs.simplecinemaapp.adapter

import ru.vsu.cs.simplecinemaapp.model.Film

interface ClickListener {
    fun onItemClick(film: Film)
}