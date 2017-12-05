package com.soumen.prithvi.svg

import android.graphics.Picture
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.caverock.androidsvg.SVG

/**
 * Created by Soumen on 04-12-2017.
 */
public class SvgDrawableTranscoder: ResourceTranscoder<SVG, PictureDrawable> {
    override public fun transcode(toTranscode: Resource<SVG>): Resource<PictureDrawable> {
        var svg: SVG = toTranscode.get()
        var picture: Picture = svg.renderToPicture()
        var drawable: PictureDrawable = PictureDrawable(picture)
        return SimpleResource<PictureDrawable>(drawable)
    }
    override public fun getId(): String {
        return ""
    }
}