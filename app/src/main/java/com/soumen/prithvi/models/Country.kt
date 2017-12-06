package com.soumen.prithvi.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Soumen on 04-12-2017.
 */
class Country {
    @SerializedName("name")
    private lateinit var name: String
    @SerializedName("topLevelDomain")
    private lateinit var topLevelDomain: Array<String>
    @SerializedName("alpha2Code")
    private lateinit var alpha2Code: String
    @SerializedName("alpha3Code")
    private lateinit var alpha3Code: String
    @SerializedName("callingCodes")
    private lateinit var callingCodes: Array<String>
    @SerializedName("capital")
    private lateinit var capital: String
    @SerializedName("altSpellings")
    private lateinit var altSpellings: Array<String>
    @SerializedName("region")
    private lateinit var region: String
    @SerializedName("subregion")
    private lateinit var subregion: String
    @SerializedName("population")
    private var population: Long? = null
    @SerializedName("latlng")
    private lateinit var latlng: Array<Double>
    @SerializedName("demonym")
    private lateinit var demonym: String
    @SerializedName("area")
    private var area: Double? = null
    @SerializedName("gini")
    private var gini: Double? = null
    @SerializedName("timezones")
    private lateinit var timezones: Array<String>
    @SerializedName("borders")
    private lateinit var borders: Array<String>
    @SerializedName("nativeName")
    private lateinit var nativeName: String
    @SerializedName("numericCode")
    private var numericCode: String? = null
    @SerializedName("currencies")
    private lateinit var currencies: List<CurrencyModel>
    @SerializedName("languages")
    private lateinit var languages: List<LanguageModel>
    @SerializedName("flag")
    private lateinit var flag: String
    @SerializedName("regionalBlocs")
    private lateinit var regionalBlocs: List<RegionalBlocModel>

    public fun setName(name: String) {
        this.name = name
    }

    public fun getName(): String {
        return name
    }

    public fun setTopLevelDomain(topLevelDomain: Array<String>) {
        this.topLevelDomain = topLevelDomain
    }

    public fun getTopLevelDomain(): Array<String> {
        return topLevelDomain
    }

    public fun setAlpha2Code(alpha2Code: String) {
        this.alpha2Code = alpha2Code
    }

    public fun getAlpha2Code(): String  {
        return alpha2Code
    }

    public fun setAlpha3Code(alpha3Code: String) {
        this.alpha3Code = alpha3Code
    }

    public fun getAlpha3Code(): String {
        return alpha3Code
    }

    public fun setCallingCodes(callingCodes: Array<String>) {
        this.callingCodes = callingCodes
    }

    public fun getCallingCodes(): Array<String> {
        return callingCodes
    }

    public fun setCapital(capital: String) {
        this.capital = capital
    }

    public fun getCapital(): String {
        return capital
    }

    public fun setAltSpellings(altSpellings: Array<String>) {
        this.altSpellings = altSpellings
    }

    public fun getAltSpellings(): Array<String> {
        return altSpellings
    }

    public fun setRegion(region: String) {
        this.region = region
    }

    public fun getRegion(): String {
        return region
    }

    public fun setSubregion(subregion: String) {
        this.subregion = subregion
    }

    public fun getSubregion(): String {
        return subregion
    }

    public fun setPopulation(population: Long) {
        this.population = population
    }

    public fun getPopulation(): Long {
        return population!!
    }

    public fun setLatlng(latlng: Array<Double>) {
        this.latlng = latlng
    }

    public fun getLatlng(): Array<Double> {
        return latlng
    }

    public fun setDemonym(demonym: String) {
        this.demonym = demonym
    }

    public fun getDemonym(): String {
        return demonym
    }

    public fun setArea(area: Double) {
        this.area = area
    }

    public fun getArea(): Double {
        if(area == null) {
            return 0.0
        } else {
            return area!!
        }
    }

    public fun setGini(gini: Double) {
        this.gini = gini
    }

    public fun getGini(): Double {
        if(gini == null) {
            return 0.0
        } else {
            return gini!!
        }
    }

    public fun setTimezones(timezones: Array<String>) {
        this.timezones = timezones
    }

    public fun getTimezones(): Array<String> {
        return timezones
    }

    public fun setBorders(borders: Array<String>) {
        this.borders = borders
    }

    public fun getBorders(): Array<String> {
        return borders
    }

    public fun setNativeName(nativeName: String) {
        this.nativeName = nativeName
    }

    public fun getNativeName(): String {
        return nativeName
    }

    public fun setNumericCode(numericCode: String) {
        this.numericCode = numericCode
    }

    public fun getNumericCode(): String {
        if(numericCode == null) {
            return ""
        } else {
            return numericCode!!
        }
    }

    public fun setCurrencies(currencies: List<CurrencyModel>) {
        this.currencies = currencies
    }

    public fun getCurrencies(): List<CurrencyModel> {
        return currencies
    }

    public fun setLanguages(languages: List<LanguageModel>) {
        this.languages = languages
    }

    public fun getLanguages(): List<LanguageModel> {
        return languages
    }

    public fun setFlag(flag: String) {
        this.flag = flag
    }

    public fun getFlag(): String {
        return flag
    }

    public fun setRegionalBlocs(regionalBlocs: List<RegionalBlocModel>) {
        this.regionalBlocs = regionalBlocs
    }

    public fun getRegionalBlocs(): List<RegionalBlocModel> {
        return regionalBlocs
    }
}