package ru.vsu.cs.simplecinemaapp.adapter

import ru.vsu.cs.simplecinemaapp.model.Film

interface HoldListener {
    fun onItemHold(film: Film, position: Int)
}