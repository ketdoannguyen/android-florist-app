package com.ntmk.myapp.model

class Blog {
    var title: String = ""
    var link: String = ""
    var img: String = ""
    override fun toString(): String {
        return "Blog(title='$title', link='$link', img='$img')"
    }

}