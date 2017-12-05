package com.soumen.prithvi.svg

import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.InputStream

/**
 * Created by Soumen on 04-12-2017.
 */
class SvgDecoder: ResourceDecoder<InputStream, SVG> {
    override public fun decode(source: InputStream, width: Int, height: Int): Resource<SVG> {
        try {
            var svg: SVG = SVG.getFromInputStream(source)
            return SimpleResource<SVG>(svg)
        } catch (ex: SVGParseException) {
            ex.printStackTrace()
        }
        return null!!
    }
    override public fun getId(): String {
        return "SvgDecoder.com.bumptech.svgsample.app"
    }
}