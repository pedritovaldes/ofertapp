package com.example.pedro.ofertapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pedro on 30/04/2017.
 */
public class MisAnunciosActivity extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

        private ListView mListView;

        public static final String URL = "http://10.0.2.2:80/api/v1/anuncio/6";

        private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();

        private static final String KEY_TITULO = "titulo";
        private static final String KEY_PROVINCIA = "provincia";
        private static final String KEY_SECTOR_PROF = "sector_profesional";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mis_anuncios);

            mListView = (ListView) findViewById(R.id.list_view);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(URL);
        }

        @Override
        public void onLoaded(List<Anuncio> androidList) {

            for (Anuncio anuncio : androidList) {

                HashMap<String, String> map = new HashMap<>();

                map.put(KEY_TITULO, anuncio.getTitulo());
                map.put(KEY_PROVINCIA, anuncio.getProvincia());
                map.put(KEY_SECTOR_PROF, anuncio.getSector_profesional());

                mAndroidMapList.add(map);
            }

            loadListView();
        }

        @Override
        public void onError() {
            Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Toast.makeText(this, mAndroidMapList.get(i).get(KEY_TITULO),Toast.LENGTH_LONG).show();
        }

        private void loadListView() {

            ListAdapter adapter = new SimpleAdapter(MisAnunciosActivity.this, mAndroidMapList, R.layout.list_item_anuncios,
            new String[] { KEY_TITULO, KEY_PROVINCIA, KEY_SECTOR_PROF },
            new int[] { R.id.version,R.id.name, R.id.api });

            mListView.setAdapter(adapter);
        }

    /*private User usuario_loggeado;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        this.usuario_loggeado = (User)getIntent().getExtras().getSerializable("user");

        //new AsyncGetAnuncios().execute();
    }*/
}
