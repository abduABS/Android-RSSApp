package com.example.abdurss;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> headlines;
    ArrayList<String> descriptions;
    ArrayList<String> dates;
    ArrayList<String> images;

    LayoutInflater inflater;

    public CustomBaseAdapter(Context context, ArrayList<String> headlines, ArrayList<String> descriptions, ArrayList<String> dates, ArrayList<String> images){
        this.context = context;
        this.headlines = headlines;
        this.descriptions = descriptions;
        this.images = images;
        this.dates = dates;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return headlines.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_item, null);
        TextView tv_headline = view.findViewById(R.id.tv_headline);
        TextView tv_description = view.findViewById(R.id.tv_description);
        TextView tv_date = view.findViewById(R.id.tv_date);
        ImageView imageView = view.findViewById(R.id.imageView);

        tv_headline.setText(headlines.get(i));
        tv_description.setText(descriptions.get(i));
        tv_date.setText(dates.get(i));

        new DownloadImageTask(imageView).execute(images.get(i));



        return view;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
