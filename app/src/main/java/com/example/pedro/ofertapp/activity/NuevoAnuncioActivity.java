package com.example.pedro.ofertapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pedro.ofertapp.R;
import com.example.pedro.ofertapp.model.User;

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

/**
 * Created by Pedro on 09/01/2017.
 */
public class NuevoAnuncioActivity extends AppCompatActivity {

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

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent= new Intent();
                intent.putExtra("user", usuario_loggeado);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            pdLoading.setMessage("Enviando...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://10.0.2.2:80/api/v1/anuncio");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }

            try  {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //Agregamos parametros a la URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("titulo", params[0])
                        .appendQueryParameter("sector_profesional", params[1])
                        .appendQueryParameter("provincia", params[2])
                        .appendQueryParameter("precio_maximo", params[3])
                        .appendQueryParameter("descripcion", params[4])
                        .appendQueryParameter("user_id", usuario_loggeado.getId().toString());

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {

                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    //Leemos lo que nos devuelve el servidor
                    InputStream input = conn.getInputStream();
                    String result = convertStreamToString(input);
                    input.close();

                    //Pasamos a onPostExecute
                    return result.toString();

                } else {
                    return ("unsuccesful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            pdLoading.dismiss();

            String message;
            Integer code;

            try {

                JSONObject obj = new JSONObject(result);
                code = obj.getInt("code");

                if(code.equals(201)) {
                    Toast.makeText(NuevoAnuncioActivity.this, R.string.mensaje_ok_nuevo_anuncio, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    i.putExtra("user", usuario_loggeado);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);

                } else if(code.equals(400)) {
                    Toast.makeText(NuevoAnuncioActivity.this, R.string.mensaje_fail_nuevo_anuncio, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
