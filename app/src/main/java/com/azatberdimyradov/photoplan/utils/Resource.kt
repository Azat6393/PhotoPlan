package com.azatberdimyradov.photoplan.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String?): Resource<T>(null,message)
    class Empty<T>: Resource<T>(null,null)
}