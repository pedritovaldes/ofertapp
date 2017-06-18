package com.example.pedro.ofertapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
 * Created by Pedro on 19/12/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    private TextView mensaje_inicio;
    private User usuario_loggeado;

    private Button button_eliminar_perfil;
    private Button button_nuevo_anuncio;
    private Button button_mis_anuncios;
    private Button button_mi_perfil;
    private Button button_buscar;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        usuario_loggeado = (User)getIntent().getExtras().getSerializable("user");

        mensaje_inicio = (TextView)findViewById(R.id.mensaje_inicio);
        mensaje_inicio.setText("Hola " + usuario_loggeado.getNombre());

        button_buscar = (Button) findViewById(R.id.button_buscar);
        button_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarBuscarAnuncio(usuario_loggeado);
            }
        });

        button_eliminar_perfil=(Button) findViewById(R.id.eliminar_perfil);
        button_eliminar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfile();
            }
        });

        button_nuevo_anuncio = (Button)findViewById(R.id.publicar_anuncio);
        button_nuevo_anuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarNuevoAnuncio(usuario_loggeado);
            }
        });

        button_mis_anuncios = (Button)findViewById(R.id.mis_anuncios);
        button_mis_anuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarMisAnuncios(usuario_loggeado);
            }
        });

        button_mi_perfil = (Button)findViewById(R.id.mi_perfil);
        button_mi_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarMiPerfil(usuario_loggeado);
            }
        });
    }

    public void lanzarBuscarAnuncio(User user) {
        Intent i = new Intent(this, BuscarAnuncioActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }

    public void lanzarMiPerfil(User user) {
        Intent i = new Intent(this, EditarPerfilActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }

    public void lanzarNuevoAnuncio(User user) {
        Intent i = new Intent(this, NuevoAnuncioActivity.class);
        i.putExtra("user",user);
        startActivityForResult(i,2404);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            if(data != null) {
                usuario_loggeado = (User)data.getExtras().getSerializable("user");
            }
        }
    }

    public void lanzarMisAnuncios(User user) {
        Intent i = new Intent(this, MisAnunciosActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }

    public void deleteProfile() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setMessage("¿Desea borrar su perfil?")
                .setTitle("Confirmación")
                .setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarPerfil(usuario_loggeado);
                            }
                        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void eliminarPerfil(User user_logged) {

        new AsyncLogin().execute(user_logged.getId());
        //Intent i = new Intent(ProfileActivity.this,LoginActivity.class);
        //startActivity(i);
    }

    private class AsyncLogin extends AsyncTask<Integer, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(ProfileActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pdLoading.setMessage("Comprobando...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(Integer... params) {

            try {
                url = new URL("http://10.0.2.2:80/api/v1/user/"+params[0]);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }

            try  {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

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

            try {

                JSONObject obj = new JSONObject(result);
                code = obj.getInt("code");

                if(code.equals(200)) {

                    Toast.makeText(ProfileActivity.this, R.string.mensaje_ok_delete_user, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);

                } else {
                    Toast.makeText(ProfileActivity.this, R.string.mensaje_error_delete_user, Toast.LENGTH_LONG).show();
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
