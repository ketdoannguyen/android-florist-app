package com.ntmk.myapp.view_model

interface Listener {
    fun onSuccess()
    fun onFailure(mess : String)
}