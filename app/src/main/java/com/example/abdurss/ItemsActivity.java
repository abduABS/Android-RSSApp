package com.example.abdurss;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class ItemsActivity extends AppCompatActivity {

    ArrayList<String> headlines =  new ArrayList<>();
    ArrayList<String> descriptions =  new ArrayList<>();
    ArrayList<String> dates =  new ArrayList<>();
    ArrayList<String> links =  new ArrayList<>();
    ArrayList<String> images =  new ArrayList<>();


    ListView listView;
    BBCApp app;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        //your code starts from here…
        if(requestCode==0)
        {
            ArrayList<String> reply = intent.getStringArrayListExtra("reply");

            //Toast.makeText(this, reply ,Toast.LENGTH_SHORT).show();
            Log.d("ArrayList", "Got the arraylist");
        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(
                R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivityForResult(intent, /*request code*/0);

                // put data in the intent
                intent.putExtra("Name", "Abdu Sallouh");
                // start the intent
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.plswork);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_acitivity);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = Uri.parse(links.get(i));
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        //Getting an app
        app = (BBCApp) getApplication();
        long feedMillis = app.getFeedMillis();



        new ProcessInBackground().execute();


    }

    public InputStream getInputStream(URL url){
        try{
            return url.openConnection().getInputStream();
        }
        catch(IOException e){
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, String>{

        ProgressDialog progressDialog = new ProgressDialog(ItemsActivity.this);

        Exception exception = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Busy Loading the feed!");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... integers) {

            try{
                URL url = new URL("https://feeds.bbci.co.uk/news/world/rss.xml");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");

                boolean insideItem = false;

                int eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    //Log.d("Bruh", xpp.getName() + "Item");
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                headlines.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                descriptions.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("pubdate")) {
                            if (insideItem) {
                                dates.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("media:thumbnail")) {
                            if (insideItem) {
                                String image = xpp.getAttributeValue(null, "url");
                                //Log.d("Image", image);
                                images.add(image);
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                links.add(xpp.nextText());
                            }
                        }

                    }
                    else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem = false;
                    }

                    eventType = xpp.next();

                }

            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            } catch (XmlPullParserException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(
                    getApplicationContext(), headlines, descriptions, dates, images);

            listView.setAdapter(customBaseAdapter);
        }
    }


}