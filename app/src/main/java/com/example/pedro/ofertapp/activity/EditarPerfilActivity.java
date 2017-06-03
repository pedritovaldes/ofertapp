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
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Pedro on 21/05/2017.
 */
public class EditarPerfilActivity extends AppCompatActivity {

    private User usuario_loggeado;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    private EditText email, nombre, telf, description, pass;
    private Button edit;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        this.usuario_loggeado = (User)getIntent().getExtras().getSerializable("user");

        setView();


        edit = (Button)findViewById(R.id.button_editar);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPerfil();
            }
        });
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

    private void setView() {

        this.email = (EditText)findViewById(R.id.edit_email);
        this.email.setText(usuario_loggeado.getEmail());

        this.nombre = (EditText)findViewById(R.id.edit_nombre);
        this.nombre.setText(usuario_loggeado.getNombre());

        this.telf = (EditText)findViewById(R.id.edit_telefono);
        this.telf.setText(usuario_loggeado.getTelefono());

        this.description = (EditText)findViewById(R.id.edit_descripcion);
        this.description.setText(usuario_loggeado.getDescripcion());

        this.pass = (EditText)findViewById(R.id.edit_pass);
    }

    private void editarPerfil(){

        new AsyncLogin().execute(this.usuario_loggeado.getId().toString(),
                this.email.getText().toString(),
                this.nombre.getText().toString(),
                this.telf.getText().toString(),
                this.description.getText().toString(),
                this.pass.getText().toString());
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(EditarPerfilActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Comprobando...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://10.0.2.2:80/api/v1/user/"+Integer.parseInt(params[0]));

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }

            try  {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("PUT");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //Agregamos parametros a la URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[1])
                        .appendQueryParameter("nombre", params[2])
                        .appendQueryParameter("telefono", params[3])
                        .appendQueryParameter("password", params[5])
                        .appendQueryParameter("descripcion", params[4]);

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

            Integer code;
            String name, email, telefono, pass, descripcion;

            try {

                JSONObject obj = new JSONObject(result);
                code = obj.getInt("code");

                if(code.equals(200)) {

                    name = obj.getString("user_nombre");
                    email = obj.getString("user_email");
                    telefono = obj.getString("user_telefono");
                    pass = obj.getString("user_pass");
                    descripcion = obj.getString("user_desc");

                    usuario_loggeado.setNombre(name);
                    usuario_loggeado.setEmail(email);
                    usuario_loggeado.setTelefono(telefono);
                    usuario_loggeado.setPass(pass);
                    usuario_loggeado.setDescripcion(descripcion);

                    Toast.makeText(EditarPerfilActivity.this, R.string.mensaje_accion_ok, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    i.putExtra("user", usuario_loggeado);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);

                } else {
                    Toast.makeText(EditarPerfilActivity.this, R.string.mensaje_accion_error, Toast.LENGTH_LONG).show();
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
