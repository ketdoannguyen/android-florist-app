package com.ntmk.myapp.model

import java.io.Serializable

class FlowerCart : Serializable {
    var id: Int = 0
    var idFlower: String = ""
    var name: String = ""
    var price: String = ""
    var img: String? = null
    var quantity: Int = 0

    override fun toString(): String {
        return "FlowerCart(id=$id, idFlower='$idFlower', name='$name', price='$price', img=$img, quantity=$quantity)"
    }
}