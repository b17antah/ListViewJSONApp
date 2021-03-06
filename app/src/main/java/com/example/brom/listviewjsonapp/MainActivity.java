package com.example.brom.listviewjsonapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// Create a new class, com.example.brom.listviewjsonapp.Mountain, that can hold your JSON data

// Create a ListView as in "Assignment 1 - Toast and ListView"

// Retrieve data from Internet service using AsyncTask and the included networking code

// Parse the retrieved JSON and update the ListView adapter

// Implement a "refresh" functionality using Android's menu system


public class MainActivity extends AppCompatActivity {

    private List<Mountain> listMountain = new ArrayList<Mountain>();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Mountain m = new Mountain("Fuji", "Japan", 3776);

        adapter = new ArrayAdapter(getApplicationContext(),R.layout.textview_for_list,
                R.id.item_textView, listMountain);

        ListView myListView = (ListView)findViewById(R.id.my_listView);
        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Mountain des = (Mountain)adapter.getItem(position);

                Toast.makeText(getApplicationContext(), des.mountainInfo(), Toast.LENGTH_LONG).show();

            }
        });

        new FetchData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("testMenuStuff", "testme: "+id);

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_refresh) {
            adapter.clear();
            new FetchData().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchData extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            // These two variables need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a Java string.
            String jsonStr = null;

            try {
                // Construct the URL for the Internet service
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?login=brom");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            Log.d("kot", "DataFetched:"+o);

            try {

                // När vi har ett JSONObjekt kan vi hämta ut dess beståndsdelar
                JSONArray a = new JSONArray(o);

                for(int each = 0; each < a.length(); each++) {
                    Log.d("kot", "Mountainfound:"+each);

                    JSONObject tillfalle = a.getJSONObject(each);
                    String getId = tillfalle.getString("ID");
                    String getName = tillfalle.getString("name");
                    String getType = tillfalle.getString("type");
                    String getCompany = tillfalle.getString("company");
                    String getLocation = tillfalle.getString("location");
                    String getCategory = tillfalle.getString("category");
                    int getSize = tillfalle.getInt("size");
                    int getCost = tillfalle.getInt("cost");

                    Log.d("getID", "ID: "+getId);
                    Log.d("getName", "Name: "+getName);
                    Log.d("getType", "Type: "+getType);
                    Log.d("getCompany", "Company: "+getCompany);
                    Log.d("getLocation", "Location: "+getLocation);
                    Log.d("getCategory", "ID: "+getCategory);
                    Log.d("getSize", "Size: "+getSize);
                    Log.d("getCost", "Cost: "+getCost);

                    Mountain testMountain = new Mountain(getName, getLocation, getSize);
                    adapter.add(testMountain);
                }
            }

            catch (JSONException e) {
                Log.e("brom","E:"+e.getMessage());

            }
            // This code executes after we have received our data. The String object o holds
            // the un-parsed JSON string or is null if we had an IOException during the fetch.

            // Implement a parsing code that loops through the entire JSON and creates objects
            // of our newly created com.example.brom.listviewjsonapp.Mountain class.
        }
    }
}

