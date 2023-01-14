package com.example.tagcloud.elements

import android.util.Log

class TextWithMissingElement(
    val title: String,
    mask: String,
    val missings: List<String>
) {
    var content: ArrayList<TextForMissing> = ArrayList()
    var countmissings = 0

    init {
        for (elem in mask.split(" ")) {
            if (elem == "{{}}") {
                if (countmissings > missings.size) {
                    content.clear()
                    content.add(SimpleText(mask))
                    break
                }
                content.add(TextMissing(missings[countmissings], missings[countmissings].length))
                countmissings += 1
            } else {
                content.add(SimpleText(elem))
            }
        }
    }
}

open class TextForMissing() {
    open val text: String = ""
    override fun toString(): String {
        return text
    }
}

class TextMissing(intext: String, val size: Int) : TextForMissing() {
    override val text = intext

}

class SimpleText(intext: String) : TextForMissing() {
    override val text = intext
}
