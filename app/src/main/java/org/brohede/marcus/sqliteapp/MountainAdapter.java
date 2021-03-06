package org.brohede.marcus.sqliteapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;


public class MountainAdapter extends ArrayAdapter<Mountain> {

    public MountainAdapter(Context context, ArrayList<Mountain> mountainList) {
        super(context, 0, mountainList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        String mountainIMG = "";

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Mountain currentMountain = getItem(position);

        //Skriv ut namnet på berget
        TextView nameTextView = listItemView.findViewById(R.id.mountName);
        nameTextView.setText(currentMountain.toString());

        //Skriv ut information om berget
        TextView numberTextView = listItemView.findViewById(R.id.mountInfo);
        numberTextView.setText(currentMountain.info());

        try {
            mountainIMG = Mountain.splitAuxdata(currentMountain.getAuxdata(), "img");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Ladda ner och visa bilden
        new DownloadImageTask((ImageView) listItemView.findViewById(R.id.list_item_icon)).execute(mountainIMG);

        return listItemView;
    }


    //Ladda bilder ifrån en URL
    //Tagen ifrån https://stackoverflow.com/a/9288544/3822307
    static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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