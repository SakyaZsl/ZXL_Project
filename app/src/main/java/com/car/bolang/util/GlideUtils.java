package com.car.bolang.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.car.bolang.R;

public class GlideUtils {

    //RequestOptions.bitmapTransform(new CircleCrop())  圆形

    public static void basisGlide(Context context, Object url, RequestOptions requestOptions, ImageView view) {
        if (requestOptions != null) {
            Glide.with(context).load(url).apply(requestOptions).into(view);
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.img_default)//图片加载出来前，显示的图片
                    .fallback( R.drawable.img_default) //url为空的时候,显示的图片
                    .error(R.drawable.img_default);//图片加载失败后，显示的图片

            Glide.with(context).load(url).apply(options).into(view);
        }
    }
}
