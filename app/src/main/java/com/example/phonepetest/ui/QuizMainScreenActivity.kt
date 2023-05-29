package com.example.phonepetest.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.phonepetest.adapters.ClickInterface
import com.example.phonepetest.adapters.LettersAdapter
import com.example.phonepetest.databinding.ActivityQuizMainScreenBinding
import com.example.phonepetest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class QuizMainScreenActivity : AppCompatActivity() {


    private lateinit var binding: ActivityQuizMainScreenBinding

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var letterAdapter: LettersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvLetters.adapter = letterAdapter

        letterAdapter.setItemClick(object : ClickInterface<String> {
            override fun onClick(data: String) {
                val newAnswer = binding.tvAnswer.text.toString() + data
                binding.tvAnswer.setText(newAnswer)
            }
        })

        mainViewModel.observeQuizList()?.observe(this) {
            val listString = it.name.toList()
            letterAdapter.updateLetters(listString)
            Glide
                .with(binding.root.context)
                .load(it.imgUrl)
                .centerCrop()
                .into(binding.ivLogo)
        }

        binding.tvAnswer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty())
                    mainViewModel.checkQuizAnswer(s.toString())
            }
        })
    }
}