package com.altamirano.fabricio.notifications.database

interface CallbackSql<T> {
    fun onResult(result:T)
}