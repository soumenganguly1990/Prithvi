package com.soumen.prithvi.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Soumen on 04-12-2017.
 */
class LanguageModel {
    @SerializedName("iso639_1")
    private lateinit var iso639_1: String
    @SerializedName("iso639_2")
    private lateinit var iso639_2: String
    @SerializedName("name")
    private lateinit var name: String
    @SerializedName("nativeName")
    private lateinit var nativeName: String

    public fun setiso639_1(iso639_1: String) {
        this.iso639_1 = iso639_1
    }

    public fun getiso639_1(): String {
        return iso639_1
    }

    public fun setiso639_2(iso639_2: String) {
        this.iso639_2 = iso639_2
    }

    public fun getiso639_2(): String {
        return iso639_2
    }

    public fun setName(name: String) {
        this.name = name
    }

    public fun getName(): String {
        return name
    }

    public fun setNativeName(nativeName: String) {
        this.nativeName = nativeName
    }

    public fun getNativeName(): String {
        return nativeName
    }

    override fun toString(): String {
        if(name != null) {
            if(nativeName != null) {
                return "$name($nativeName)"
            } else {
                return name
            }
        } else {
            return name!!
        }
    }
}