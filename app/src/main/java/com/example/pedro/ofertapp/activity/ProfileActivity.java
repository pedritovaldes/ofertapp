package com.example.pedro.ofertapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.example.pedro.ofertapp.R.id.info;

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

    private Client client;
    private WebTarget target;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_new).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_logout:
                confirmLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void confirmLogout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setMessage(R.string.logoutMessage)
                .setTitle(R.string.confirmTitle)
                .setPositiveButton(R.string.optionYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(ProfileActivity.this, LoginActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        })
                .setNegativeButton(R.string.optionCancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
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

        new AsyncDeleteProfile().execute(user_logged.getId());
    }

    private class AsyncDeleteProfile extends AsyncTask<Integer, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(ProfileActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(Integer... params) {

            client = ClientBuilder.newClient();
            target = client.target(getString(R.string.emulatorDeviceEndpoint));

            target = target.path("user/"+params[0]);

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .delete(Response.class);

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
}
