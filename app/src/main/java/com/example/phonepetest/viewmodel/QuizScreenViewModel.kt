package com.example.phonepetest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonepetest.dataClasses.QuizData
import com.example.phonepetest.repository.QuizRepo
import com.example.phonepetest.warperClasses.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizScreenViewModel @Inject constructor(private val quizRepo: QuizRepo) : ViewModel() {

    init {
        getQuizData()
    }

    private var quizPosition: Int = 0
    private lateinit var quizList: List<QuizData>
    var quizListLiveData = MutableLiveData<QuizData>()

    fun observeQuizList() = quizListLiveData as? LiveData<QuizData>

    private fun getQuizData() {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepo.getQuizData().collect {

                when (it) {
                    is NetworkResult.Loading -> {
                    }

                    is NetworkResult.Failure -> {
                    }

                    is NetworkResult.Success -> {
                        quizList = it.data
                        updateQuizPosition()
                    }
                }

            }

        }
    }

    private fun submitQuiz() {
        quizPosition++
    }

    private fun updateQuizPosition() {
        if (quizList.size-1 > quizPosition)
            quizListLiveData.postValue(quizList[quizPosition])
    }

    fun checkQuizAnswer(answer: String) {
        if (quizList[quizPosition].name == answer) {
            submitQuiz()
            updateQuizPosition()
        }
    }

}