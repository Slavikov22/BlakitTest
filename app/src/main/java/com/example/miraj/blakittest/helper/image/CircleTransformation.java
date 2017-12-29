package com.example.miraj.blakittest.helper.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

public class CircleTransformation implements Transformation{
    @Override
    public Bitmap transform(Bitmap source) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawCircle(source.getWidth() / 2, source.getWidth() / 2, source.getWidth() / 2, paint);

        if (source != output)
            source.recycle();

        return output;
    }

    @Override
    public String key() {
        return "circle";
    }
}
