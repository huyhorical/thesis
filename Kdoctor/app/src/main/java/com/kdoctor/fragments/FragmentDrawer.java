package com.kdoctor.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kdoctor.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FragmentDrawer extends Fragment {

    private static FragmentDrawer fragmentDrawer;
    public static FragmentDrawer getInstant(){
        return fragmentDrawer;
    }

    public ImageView ivDrawerAvatar;
    TextView tvDrawerName, tvDrawerLocation, tvCurVelocity, tvVerhicle, tvEditInfo, tvLogout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentDrawer = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentDrawer = this;
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);

        return view;
    }

    public String getAddress(Context context, double lat, double lng) {
        if (lat == 0.0 && lng == 0.0)
            return "Vị trí của tôi: đang nhập dữ liệu";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            String add = "";
            String str0, str1, str2, str3, str4;

            Address obj = addresses.get(0);
            str0 = obj.getAddressLine(0);
            str1 = obj.getAddressLine(1);
            str2 = obj.getAddressLine(2);
            str3 = obj.getAddressLine(3);
            str4 = obj.getAddressLine(4);

            if (str0 != null && !str0.equals(""))
                add += str0 + "\n";
            if (str1 != null && !str1.equals(""))
                add += str1 + " - ";
            if (str2 != null && !str2.equals(""))
                add += str2  + "\n";
            if (str3 != null && !str3.equals(""))
                add += str3 + " - ";
            if (str4 != null && !str4.equals(""))
                add += str4;

            for (int i = 0; i < addresses.size(); i++) {
                Log.d("=Adress=",addresses.get(i).toString() + " ------------ ");
            }


            return add;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap transformAvatar(Bitmap source) {
        try {

            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap
                    .createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                // source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    squaredBitmap.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            // canvas.drawArc(rectf, -90, 360, false, lightRed);
            // squaredBitmap.recycle();
            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return source;
    }

}
