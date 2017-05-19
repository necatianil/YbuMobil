package com.example.asus.ybumobil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ASUS on 8.05.2017.
 */

public class AnnouncementsFragmentClass extends Fragment {
    private static final String TAG="Announcements";
    private ListView listView;
    ProgressDialog progressDialog;
    private ArrayList listofannouncement ;
    private ArrayAdapter<String> announcementadapdter;
    private ArrayList linklist;

    public AnnouncementsFragmentClass() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.announcement_fragment,container,false);
        listView=(ListView)view.findViewById(R.id.announcementlist);
        listofannouncement = new ArrayList<>();
        linklist=new ArrayList();
        announcementadapdter=new ArrayAdapter<String>
                (getActivity().getApplicationContext(),R.layout.list_layout,R.id.adaptertext,listofannouncement);

        if(internet_connection()) {
            new getFromSite().execute();
            clickitem();
        }
        else{
            //create a snackbar telling the user there is no internet connection and issuing a chance to reconnect
            final Snackbar snackbar = Snackbar.make(container,
                    "No internet connection.",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),
                    R.color.unselectedtext));
            snackbar.setAction(R.string.try_again, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(internet_connection()){
                        new getFromSite().execute();
                        clickitem();
                    }
                    else{
                        onCreateView(inflater,container,savedInstanceState);
                    }
                }
            }).show();

        }

        return view;
    }
    private class getFromSite extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc= Jsoup.connect("http://www.ybu.edu.tr/muhendislik/bilgisayar/").get();
                Elements element=doc.select("div.caContent>div.cncItem a");
                Elements allannouncements=doc.select("div.caButton a");
                for(int i= 0;i<element.size();i++){
                    listofannouncement.add(element.get(i).text());
                    linklist.add(element.get(i).attr("href"));
                }
                String str=allannouncements.text().toUpperCase();
                listofannouncement.add(str);
                linklist.add(allannouncements.attr("href"));


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listView.setAdapter(announcementadapdter);
            progressDialog.dismiss();
        }

    }
    public void clickitem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse("http://www.ybu.edu.tr/muhendislik/bilgisayar/"+linklist.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
