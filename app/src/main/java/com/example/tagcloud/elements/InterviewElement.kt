package com.example.tagcloud.elements

import android.util.Log

class InterviewData(
    val title: String,
    val mapAnswers: List<Pair<String, List<String>>>,
    val countAnswers: MutableList<MutableList<Int>>
) {
    fun getCountQuestions(): Int {
        return mapAnswers.size
    }

    fun getTitle(index: Int): String? {
        if (index >= getCountQuestions()) {
            return null
        }
        return mapAnswers[index].first
    }

    fun getCountInBlock(index: Int): Int {
        if (index >= getCountQuestions()) {
            return 0
        }
        return mapAnswers[index].second.size
    }

    fun getAnswerInBlock(index: Int, answCount: Int): String {
        if (index >= getCountQuestions()) {
            return "Null"
        }
        return mapAnswers[index].second[answCount]
    }
    fun addVote(index: Int, answCount: Int){
        if (index < getCountQuestions()) {
            countAnswers[index][answCount] += 1
        }
    }
    fun voteInAnswer(index: Int, answCount: Int): Int {
        if (index >= getCountQuestions()) {
            return 0
        }


        return countAnswers[index][answCount]
    }
    fun voteInAnswerPercent(index: Int, answCount: Int): Int {
        if (index >= getCountQuestions()) {
            return 0
        }
        return ((voteInAnswer(index, answCount) / sumVote(index).toFloat()) * 100).toInt()
    }
    fun sumVote(index: Int): Int {
        if (index >= getCountQuestions()) {
            return 0
        }
        return countAnswers[index].sum()
    }
}