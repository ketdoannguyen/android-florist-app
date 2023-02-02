package com.ntmk.myapp.model

class Chat {
    var id : Int = 0
    var idSender : String = ""
    var nameSender : String = ""
    var imgSender : String = ""
    var message : String = ""

    constructor()
    constructor(id: Int, idSender: String,nameSender: String,imgSender: String, message: String) {
        this.id = id
        this.idSender = idSender
        this.nameSender = nameSender
        this.imgSender = imgSender
        this.message = message
    }

    constructor(idSender:String,nameSender: String,imgSender: String,message: String) {
        this.idSender = idSender
        this.nameSender = nameSender
        this.imgSender = imgSender
        this.message = message
    }



}


