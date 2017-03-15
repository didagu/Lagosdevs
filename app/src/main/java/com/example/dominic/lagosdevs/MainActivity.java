package com.example.dominic.lagosdevs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    ProgressBar progressBar;
    DevAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        //instantiate and set adapter for the listview
        adapter=new DevAdapter(this,new ArrayList<Dev>());
        mListView=(ListView) findViewById(R.id.items_list_view);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Dev dev=(Dev) parent.getItemAtPosition(position);
                Intent myIntent = new Intent(view.getContext(), ProfileDetailsActivity.class);
                myIntent.putExtra("dev",dev);
                startActivity(myIntent);
            }
        });

        //execute async task
        new getDataTask().execute();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.about:
                startActivity(new Intent(this,AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    class getDataTask extends AsyncTask<String, Void, ArrayList<Dev>>{

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Dev> doInBackground(String... params) {
            ArrayList<Dev> response=new ArrayList<>();
            //api call url to get java developers from lagos
            String link="https://api.github.com/search/users?q=location:lagos+language:java&" +
                    "access_token=a668b1b8007722d00953241ad2bd17b6cf3d5529";

            try {
                URL url=new URL(link);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line;
                while((line=reader.readLine())!=null){
                    sb.append(line+"\n");
                }

                JSONObject jsonObject= new JSONObject(sb.toString());
                JSONArray jsonArray=jsonObject.getJSONArray("items");
                Log.i("Response: ", ">" + sb.toString());
                //looping through all the users
                for(int i=0;i< jsonArray.length();i++){
                    response.add(convertDev(jsonArray.getJSONObject(i)));
                }
                conn.disconnect();
                return response;
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(ArrayList<Dev> response) {
            super.onPostExecute(response);
            progressBar.setVisibility(View.GONE);
            adapter.setItemsList(response);
            adapter.notifyDataSetChanged();
        }

        //parsing the users' data from the JSON object
        private Dev convertDev(JSONObject obj) throws JSONException {
            String username = obj.getString("login");
            String photoUrl = obj.getString("avatar_url");
            String profileUrl = obj.getString("html_url");

            return new Dev(username, profileUrl,photoUrl);
        }
    }
}
