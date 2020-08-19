package com.example.gxscanner

import java.lang.StringBuilder


object DocumentTemplate{
    fun getHTML(results:ArrayList<String>, template:String):String{
        var htmlBuilder=StringBuilder()
        htmlBuilder.append("<ul>")
        results.forEach {
            htmlBuilder.append("<li>")
            htmlBuilder.append(it)
            htmlBuilder.append("</li>")
        }
        htmlBuilder.append("</ul>")
        return template.replace("%%REPLACE%%",htmlBuilder.toString())
    }
}