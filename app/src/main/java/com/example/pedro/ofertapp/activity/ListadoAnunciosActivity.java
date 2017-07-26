package com.example.pedro.ofertapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.pedro.ofertapp.LoadJSONTask;
import com.example.pedro.ofertapp.LoadListadoAnunciosTask;
import com.example.pedro.ofertapp.R;
import com.example.pedro.ofertapp.model.Anuncio;
import com.example.pedro.ofertapp.model.ListadoAnuncios;
import com.example.pedro.ofertapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pedro on 04/07/2017.
 */
public class ListadoAnunciosActivity extends AppCompatActivity implements LoadListadoAnunciosTask.Listener, AdapterView.OnItemClickListener{

    private ListView mListView;
    private User usuario_loggeado;
    public static String URL = "http://10.0.2.2:80/api/v1/anuncio/";

    private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();

    private static final String KEY_ID = "id";
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_PROVINCIA = "provincia";
    private static final String KEY_SECTOR_PROF = "sector_profesional";
    private static final String KEY_PRECIO_MAX = "precio_maximo";
    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TELEFONO = "telefono";


    private String provincia, sector, precio, fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_anuncios);

        Intent intent = getIntent();

        this.usuario_loggeado = (User)getIntent().getExtras().getSerializable("user");
        this.provincia = intent.getStringExtra("provincia");
        this.sector = intent.getStringExtra("sector");
        this.precio = intent.getStringExtra("precio");
        this.fecha = intent.getStringExtra("fecha");

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);

        this.URL = URL + sector + "/" + provincia
                + "/" + precio;

        //new LoadListadoAnunciosTask(this).execute(URL);
        new LoadListadoAnunciosTask(this).execute(URL);
    }

    @Override
    public void onLoaded(List<ListadoAnuncios> androidList) {

        for (ListadoAnuncios listadoanuncio : androidList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_ID, listadoanuncio.getId().toString());
            map.put(KEY_TITULO, listadoanuncio.getTitulo());
            map.put(KEY_PROVINCIA, listadoanuncio.getProvincia());
            map.put(KEY_SECTOR_PROF, listadoanuncio.getSector_profesional());
            map.put(KEY_PRECIO_MAX, listadoanuncio.getPrecio_maximo().toString());
            map.put(KEY_DESCRIPCION, listadoanuncio.getDescripcion());
            map.put(KEY_NAME, listadoanuncio.getName());
            map.put(KEY_EMAIL, listadoanuncio.getEmail());
            map.put(KEY_TELEFONO, listadoanuncio.getTelefono());

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
        /*Intent detalleIntent = new Intent(this, DetalleAnuncioActivity.class);
        detalleIntent.putExtra("userLogged", usuario_loggeado);
        detalleIntent.putExtra("idAnuncio",mAndroidMapList.get(i).get(KEY_ID));
        detalleIntent.putExtra("tituloAnuncio", mAndroidMapList.get(i).get(KEY_TITULO));
        detalleIntent.putExtra("sectorAnuncio", mAndroidMapList.get(i).get(KEY_SECTOR_PROF));
        detalleIntent.putExtra("provinciaAnuncio", mAndroidMapList.get(i).get(KEY_PROVINCIA));
        detalleIntent.putExtra("precioAnuncio", mAndroidMapList.get(i).get(KEY_PRECIO_MAX));
        detalleIntent.putExtra("descripcionAnuncio", mAndroidMapList.get(i).get(KEY_DESCRIPCION));
        startActivity(detalleIntent);*/
    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(ListadoAnunciosActivity.this, mAndroidMapList, R.layout.list_item_anuncios,
                new String[] { KEY_TITULO, KEY_PROVINCIA, KEY_SECTOR_PROF },
                new int[] { R.id.titulo_anuncio,R.id.provincia_anuncio, R.id.sector_anuncio });

        mListView.setAdapter(adapter);
    }
}
