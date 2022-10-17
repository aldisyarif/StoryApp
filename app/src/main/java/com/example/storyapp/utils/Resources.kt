package com.example.storyapp.utils

import com.example.storyapp.enums.RequestStatus

class Resources<out T> private constructor(
    val requestStatus: RequestStatus,
    val data: T?,
    val message:String?
){
    companion object {
        fun <T> success(data: T?): Resources<T> {
            return Resources(
                RequestStatus.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(msg: String, data: T?): Resources<T> {
            return Resources(
                RequestStatus.ERROR,
                data,
                msg
            )
        }

        fun <T> loading(): Resources<T> {
            return Resources(
                RequestStatus.LOADING,
                null,
                null
            )
        }
    }
}