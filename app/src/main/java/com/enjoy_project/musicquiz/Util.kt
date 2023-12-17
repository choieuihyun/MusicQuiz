package com.enjoy_project.musicquiz

object Util {

    fun removeBrackets(input: String): String {
        return input.filter { it != '[' && it != ']' }
    }

}