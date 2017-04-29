package com.example.pedro.ofertapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pedro on 09/01/2017.
 */
public class NuevoAnuncioActivity extends Activity {

    private User usuario_loggeado;

    private Spinner spinner_sectores;
    private Spinner spinner_provincias;

    private String[] sectores = {"Seleccionar","Agricultura", "Minería", "Siderurgia", "Ganadería", "Pesca", "Construcción", "Industrías de procesado", "Fabricación",
    "Hostelería", "Mantenimiento", "Sanitario", "Transporte", "Investigación", "Administraciones", "Ingenieros"};

    private String[] provincias = {"Seleccionar", "Álava", "Albaecete","Alicante", "Almería"};

    private EditText tituloAnuncio;
    private Spinner sectorProfesional;
    private Spinner selectedProvincia;
    private EditText precio_max;
    private EditText descripcion;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_anuncio);

        this.usuario_loggeado = (User)getIntent().getExtras().getSerializable("user");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sectores);
        spinner_sectores = (Spinner)findViewById(R.id.nuevo_anuncio_sector);
        spinner_sectores.setAdapter(adapter);

        ArrayAdapter provincia = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provincias);
        spinner_provincias = (Spinner)findViewById(R.id.nuevo_anuncio_provincia);
        spinner_provincias.setAdapter(provincia);

        tituloAnuncio = (EditText)findViewById(R.id.nuevo_anuncio_titulo);
        sectorProfesional = (Spinner)findViewById(R.id.nuevo_anuncio_sector);
        selectedProvincia = (Spinner)findViewById(R.id.nuevo_anuncio_provincia);
        precio_max = (EditText)findViewById(R.id.nuevo_anuncio_precio_servicio);
        descripcion = (EditText)findViewById(R.id.nuevo_anuncio_texto);

        //usuario_loggeado = (User)getIntent().getExtras().getSerializable("user");

        //prueba = (EditText)findViewById(R.id.nuevo_anuncio_titulo);
        //prueba.setText(usuario_loggeado.getNombre());
    }

    public void nuevoAnuncio(View view) {

        String titulo = tituloAnuncio.getText().toString();
        String sectorProfesionalText = sectorProfesional.getSelectedItem().toString();
        String provinciaSeleccionadaText = selectedProvincia.getSelectedItem().toString();
        //Integer precio = Integer.parseInt(precio_max.getText().toString());
        String precio = precio_max.getText().toString();
        String textoDescripcion = descripcion.getText().toString();

        new AsyncNuevoAnuncio().execute(titulo, sectorProfesionalText, provinciaSeleccionadaText, precio, textoDescripcion);
    }

    private class AsyncNuevoAnuncio extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(NuevoAnuncioActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Cargando...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
    }
}
