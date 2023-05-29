package com.example.phonepetest.repository

import android.content.Context
import com.example.phonepetest.dataClasses.QuizData
import com.example.phonepetest.warperClasses.NetworkResult
import com.google.gson.Gson
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class QuizRepo @Inject constructor(@ApplicationContext val appContext: Context) {

    suspend fun getQuizData() = flow {
        emit(NetworkResult.Loading(true))
        val response = parseQuizDataFromRaw()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }

//    fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}

    private fun parseQuizDataFromRaw(): List<QuizData> {
        val gson = Gson()
        val jsonString = appContext.loadJSONFromAssets("quizraw.json")
        return gson.fromJson(jsonString, Array<QuizData>::class.java).asList()
    }

    private fun Context.loadJSONFromAssets(fileName: String): String {
        return applicationContext.assets.open(fileName).bufferedReader().use { reader ->
            reader.readText()
        }
    }
}