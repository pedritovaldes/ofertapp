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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pedro on 30/04/2017.
 */
public class MisAnunciosActivity extends Activity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private User usuario_loggeado;

    public static final String URL = "http://10.0.2.2:80/api/v1/anuncio/6";

    private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();

    private static final String KEY_ID = "id";
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_PROVINCIA = "provincia";
    private static final String KEY_SECTOR_PROF = "sector_profesional";
    private static final String KEY_PRECIO_MAX = "precio_maximo";
    private static final String KEY_DESCRIPCION = "descripcion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_anuncios);

        this.usuario_loggeado = (User)getIntent().getExtras().getSerializable("user");

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        new LoadJSONTask(this).execute(URL);
    }

    @Override
    public void onLoaded(List<Anuncio> androidList) {

        for (Anuncio anuncio : androidList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_ID, anuncio.getId().toString());
            map.put(KEY_TITULO, anuncio.getTitulo());
            map.put(KEY_PROVINCIA, anuncio.getProvincia());
            map.put(KEY_SECTOR_PROF, anuncio.getSector_profesional());
            map.put(KEY_PRECIO_MAX, anuncio.getPrecio_maximo().toString());
            map.put(KEY_DESCRIPCION, anuncio.getDescripcion());

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

        //Toast.makeText(this, mAndroidMapList.get(i).get(KEY_TITULO),Toast.LENGTH_LONG).show();
        Intent detalleIntent = new Intent(this, DetalleAnuncio.class);
        detalleIntent.putExtra("userLogged", usuario_loggeado);
        detalleIntent.putExtra("idAnuncio",mAndroidMapList.get(i).get(KEY_ID));
        detalleIntent.putExtra("tituloAnuncio", mAndroidMapList.get(i).get(KEY_TITULO));
        detalleIntent.putExtra("sectorAnuncio", mAndroidMapList.get(i).get(KEY_SECTOR_PROF));
        detalleIntent.putExtra("provinciaAnuncio", mAndroidMapList.get(i).get(KEY_PROVINCIA));
        detalleIntent.putExtra("precioAnuncio", mAndroidMapList.get(i).get(KEY_PRECIO_MAX));
        detalleIntent.putExtra("descripcionAnuncio", mAndroidMapList.get(i).get(KEY_DESCRIPCION));
        startActivity(detalleIntent);
    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(MisAnunciosActivity.this, mAndroidMapList, R.layout.list_item_anuncios,
        new String[] { KEY_TITULO, KEY_PROVINCIA, KEY_SECTOR_PROF },
        new int[] { R.id.titulo_anuncio,R.id.provincia_anuncio, R.id.sector_anuncio });

        mListView.setAdapter(adapter);
    }
}
