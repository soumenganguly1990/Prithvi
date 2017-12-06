package com.soumen.prithvi.adapters;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.soumen.prithvi.R;
import com.soumen.prithvi.dbops.CountryModel;
import com.soumen.prithvi.models.Country;
import com.soumen.prithvi.svg.SvgDecoder;
import com.soumen.prithvi.svg.SvgDrawableTranscoder;
import com.soumen.prithvi.svg.SvgSoftwareLayerSetter;
import java.io.InputStream;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Soumen on 04-12-2017.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryHolder> {

    private int lastPosition = -1;
    Context context;
    ArrayList<CountryModel> countryList;

    public CountryAdapter(Context context, ArrayList<CountryModel> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    @Override
    public CountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_list, parent, false);
        CountryHolder ch = new CountryHolder(v);
        return ch;
    }

    @Override
    public void onBindViewHolder(CountryHolder holder, int position) {
        CountryModel country = countryList.get(position);
        if (country.getFlag() == null || country.getFlag().equalsIgnoreCase("")) {
            holder.imgFlag.setImageResource(android.R.drawable.stat_sys_warning);
            Glide.clear(holder.imgFlag);
        } else {
            loadSvgFlagWithGlide(holder.imgFlag, country.getFlag());
        }
        holder.txtCountryName.setText(country.getName());
        holder.txtCountryCapital.setText(country.getCapital());
        if (country.getSubregion() != null || !country.getSubregion().equalsIgnoreCase("")) {
            holder.txtCountryRegion.setText(country.getSubregion());
        }
        if (country.getRegion() != null || !country.getRegion().equalsIgnoreCase("")) {
            holder.txtCountryRegion.setText(holder.txtCountryRegion.getText().toString() + ", " + country.getRegion());
        }
        holder.txtCountryCurrency.setText(countryList.get(position).getCurrencies());
        setAnimation(holder.itemView, position);
    }

    /**
     * Drops the [, ] characters from the a list's tostring equiv return
     *
     * @param someList
     * @return
     */
    private String getFormattedString(ArrayList someList) {
        try {
            String str = someList.toString();
            if (str == null) {
                str = "";
            } else {
                str = str.replace("[", "");
                str = str.replace("]", "");
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Loads the svg images into the imageview with the help of androidsvg and glide
     *
     * @param imgView
     * @param url
     */
    private void loadSvgFlagWithGlide(final ImageView imgView, final String url) {
            Handler mHandler = new Handler();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = Glide.with(context)
                            .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                            .from(Uri.class)
                            .as(SVG.class)
                            .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                            .sourceEncoder(new StreamEncoder())
                            .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                            .decoder(new SvgDecoder())
                            .thumbnail(0.05f)
                            .listener(new SvgSoftwareLayerSetter<Uri>());
                    requestBuilder
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .load(Uri.parse(url))
                            .thumbnail(0.05f)
                            .into(imgView);
                }
            });
    }

    @Override
    public int getItemCount() {
        return (null != countryList ? countryList.size() : 0);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(100);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    class CountryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgFlag)
        ImageView imgFlag;
        @BindView(R.id.txtCountryName)
        TextView txtCountryName;
        @BindView(R.id.txtCountryCapital)
        TextView txtCountryCapital;
        @BindView(R.id.txtCountryCurrency)
        TextView txtCountryCurrency;
        @BindView(R.id.txtCountryRegion)
        TextView txtCountryRegion;

        public CountryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}