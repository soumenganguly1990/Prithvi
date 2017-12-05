package com.soumen.prithvi.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Soumen on 04-12-2017.
 */
class CurrencyModel {
    @SerializedName("code")
    private var code: String? = null
    @SerializedName("name")
    private var name: String? = null
    @SerializedName("symbol")
    private var symbol: String? = null

    public fun setCode(code: String) {
        this.code = code
    }

    public fun getCode(): String? {
        return code
    }

    public fun setName(name: String) {
        this.name = name
    }

    public fun getName(): String? {
        return name
    }

    public fun setSymbol(symbol: String) {
        this.symbol = symbol
    }

    public fun getSymbol(): String? {
        return symbol
    }

    override fun toString(): String {
        if(symbol != null) {
            return "$name($symbol)"
        } else {
            return name!!
        }
    }
}