package com.ntmk.myapp.model

class Order {
    var id: String = ""
    var idUser : String = ""
    var nameUser : String = ""
    var timeOrder: String? = null
    var listFlower: ArrayList<FlowerCart>? = null
    var status: String? = null
    var payment : String = ""
    var orderTotal : String? = null
}