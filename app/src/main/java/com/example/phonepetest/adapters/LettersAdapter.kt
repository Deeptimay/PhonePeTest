package com.example.phonepetest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phonepetest.databinding.LetterSingleRowBinding
import javax.inject.Inject


class LettersAdapter @Inject constructor() :
    RecyclerView.Adapter<LettersAdapter.LettersViewHolder>() {

    private var letters = mutableListOf<Char>()
    private var clickInterface: ClickInterface<String>? = null

    fun updateLetters(letters: List<Char>) {
        val oldSize = this.letters.size
        this.letters = letters.toMutableList()
        if (oldSize == 0)
            notifyItemRangeInserted(0, letters.size)
        else
            notifyItemRangeRemoved(0, oldSize)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LettersViewHolder {
        val binding =
            LetterSingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LettersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LettersViewHolder, position: Int) {
        val letter = letters[position]
        holder.view.letters.text = letter.toString()

        holder.view.letters.setOnClickListener {
            clickInterface?.onClick(letter.toString())
        }
    }

    override fun getItemCount(): Int {
        return letters.size
    }

    fun setItemClick(clickInterface: ClickInterface<String>) {
        this.clickInterface = clickInterface
    }

    class LettersViewHolder(val view: LetterSingleRowBinding) : RecyclerView.ViewHolder(view.root)
}

interface ClickInterface<T> {
    fun onClick(data: T)
}