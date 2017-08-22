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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Pedro on 23/11/2016.
 */
public class RegistroActivity extends AppCompatActivity{

    private Button registro;
    private EditText etNombre;
    private EditText etTelefono;
    private EditText etEmail;
    private EditText etDescripcion;

    private Client client;
    private WebTarget target;

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

        //new AsyncRegistro().execute(nombre, telf, email, desc);
        new AsyncRegistroJerseyClient().execute(nombre, telf, email, desc);
    }

    private class AsyncRegistroJerseyClient extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(RegistroActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Comprobando...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            client = ClientBuilder.newClient();
            target = client.target(getString(R.string.emulatorDeviceEndpoint));


            JSONObject json = new JSONObject();
            try {
                json.put("nombre", params[0]);
                json.put("telefono", params[1]);
                json.put("email", params[2]);
                json.put("descripcion", params[3]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String input = json.toString();
            target = target.path("registro");

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(input, MediaType.APPLICATION_JSON),Response.class);

            if(response.getStatus() == 200) {
                String output = response.readEntity(String.class);
                return output;
            } else {
                return ("unsuccesful");
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
}
