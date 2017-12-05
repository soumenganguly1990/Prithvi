package com.soumen.prithvi.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Soumen on 04-12-2017.
 */
class RegionalBlocModel {
    @SerializedName("acronym")
    private lateinit var acronym: String
    @SerializedName("name")
    private lateinit var name: String

    public fun setAcronym(acronym: String) {
        this.acronym = acronym
    }

    public fun getAcronym(): String {
        return acronym
    }

    public fun setName(name: String) {
        this.name = name
    }

    public fun getName(): String {
        return name
    }
}