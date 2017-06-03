package com.example.pedro.ofertapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pedro.ofertapp.R;
import com.example.pedro.ofertapp.TextValidator;

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
 * Created by Pedro on 23/11/2016.
 */
public class RegistroActivity extends AppCompatActivity{

    private Button registro;
    private EditText etNombre;
    private EditText etTelefono;
    private EditText etEmail;
    private EditText etDescripcion;

    //private Context context;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        //Recogemos las referencias
        etNombre = (EditText)findViewById(R.id.registro_nombre);
        etNombre.addTextChangedListener(new TextValidator(etNombre) {
            @Override
            public void validate(EditText editText, String text) {
                if(text.equals(""))
                    etNombre.setError("El nombre es obligatorio");
            }
        });

        etTelefono = (EditText)findViewById(R.id.registro_telefono);

        etEmail = (EditText)findViewById(R.id.registro_email);
        etEmail.addTextChangedListener(new TextValidator(etEmail) {
            @Override
            public void validate(EditText editText, String text) {
                if(text.equals(""))
                    etEmail.setError("El email es obligatorio");
            }
        });

        etDescripcion = (EditText)findViewById(R.id.registro_descripcion);
    }

    public void registro(View view) {

        String nombre = etNombre.getText().toString();
        String telf = etTelefono.getText().toString();
        String email = etEmail.getText().toString();
        String desc = etDescripcion.getText().toString();

        new AsyncRegistro().execute(nombre, telf, email, desc);
    }

    private class AsyncRegistro extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(RegistroActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Cargando...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://10.0.2.2:80/api/v1/registro");

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
                        .appendQueryParameter("nombre", params[0])
                        .appendQueryParameter("telefono", params[1])
                        .appendQueryParameter("email", params[2])
                        .appendQueryParameter("descripcion", params[3]);

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
                    Toast.makeText(RegistroActivity.this, R.string.mensaje_ok_registro, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);

                } else if(code.equals(400)) {
                    Toast.makeText(RegistroActivity.this, R.string.mensaje_fail_registro, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegistroActivity.this, R.string.mensaje_error_registro, Toast.LENGTH_LONG).show();
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
