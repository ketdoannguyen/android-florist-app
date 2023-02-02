package com.ntmk.myapp.model

import java.io.Serializable

data class User(
    var userId: String? = null,
    var userName: String? = null,
    var fullName: String? = null,
    var email: String? = null,
    var pass: String? = null,
    var phone: String? = null,
    var address:String? = null,
    var birthday: String? = null,
    var img : String? = null


) {
    override fun toString(): String {
        return "User(userId=$userId, userName=$userName, fullName=$fullName, email=$email, pass=$pass, phone=$phone, address=$address, birthday=$birthday, img=$img)"
    }
}