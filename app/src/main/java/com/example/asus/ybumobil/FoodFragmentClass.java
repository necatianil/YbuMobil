package com.example.asus.ybumobil;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ASUS on 8.05.2017.
 */

public class FoodFragmentClass extends Fragment {
   TextView tdate,t1,t2,t3,t4;
    ProgressDialog progressDialog;
    ArrayList listoffood = new ArrayList<>();
    CoordinatorLayout clayout;
   // ArrayAdapter<String> foodadapdter;
    private static final String TAG="Food";
    public FoodFragmentClass() {

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.food_fragment,container,false);
        tdate=(TextView)view.findViewById(R.id.fooddate);
        t1=(TextView)view.findViewById(R.id.food1);
        t2=(TextView)view.findViewById(R.id.food2);
        t3=(TextView)view.findViewById(R.id.food3);
        t4=(TextView)view.findViewById(R.id.food4);
        clayout=(CoordinatorLayout) view.findViewById(R.id.snackbarlayout);
        //  listView=(ListView)view.findViewById(R.id.foodlist);
      //  foodadapdter=new ArrayAdapter<String>
      //          (getActivity(),R.layout.list_layout,R.id.foodtext,listoffood);
        if(internet_connection()) {
            new getFromSite().execute();
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
               }
               else{
                   onCreateView(inflater,container,savedInstanceState);
               }
            }
        }).show();

        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        listoffood.clear();
    }

    private class getFromSite extends AsyncTask<Void,Void,Void>{

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
                Document doc= Jsoup.connect("http://ybu.edu.tr/sks/").get();
                    Elements date = doc.select("h3");
                    Elements element = doc.select("h5");
                    listoffood.add(date.text());
                    for (int i = 0; i < element.size(); i++) {
                        listoffood.add(element.get(i).text());
                    }

            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
         //  listView.setAdapter(foodadapdter);
            tdate.setText(listoffood.get(0).toString());
            t1.setText(listoffood.get(1).toString());
            t2.setText(listoffood.get(2).toString());
            t3.setText(listoffood.get(3).toString());
            t4.setText(listoffood.get(4).toString());
            progressDialog.dismiss();
        }

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
