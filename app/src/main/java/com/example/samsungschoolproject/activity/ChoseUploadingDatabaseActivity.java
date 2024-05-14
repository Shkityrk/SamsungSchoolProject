package com.example.samsungschoolproject.activity;

import static com.example.samsungschoolproject.utils.NetworkUtils.disableSSLCertificateChecking;


import android.os.Bundle;
import android.util.Log;
import android.os.AsyncTask;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.adapter.DatabaseAdapter;
import com.example.samsungschoolproject.model.DatabaseModel;
import com.example.samsungschoolproject.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChoseUploadingDatabaseActivity extends AppCompatActivity {

        private RecyclerView recyclerView;
        private DatabaseAdapter adapter;
        private List<DatabaseModel> databaseList = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_database_list);

            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new DatabaseAdapter(databaseList, getApplication(), getApplicationContext());
            recyclerView.setAdapter(adapter);

            new FetchDataTask().execute();

        }

    private class FetchDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            disableSSLCertificateChecking();
            String url = "https://79.137.197.216/list_databases/";
            String jsonString = NetworkUtils.getJSONFromServer(url);
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            try {
                assert jsonString != null;
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("databases");

                Log.d("DATABASES", jsonArray.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject databaseObject = jsonArray.getJSONObject(i);
                    String name = databaseObject.getString("name");
                    String nameDev = databaseObject.getString("nameGET");
                    String database = databaseObject.getString("database");
                    DatabaseModel databaseModel = new DatabaseModel(name, nameDev, database);
                    databaseList.add(databaseModel);
                }

                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    }
