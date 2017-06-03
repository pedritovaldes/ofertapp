package com.example.pedro.ofertapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
 * Created by Pedro on 06/05/2017.
 */
public class DetalleAnuncioActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    private User usuario_loggeado;
    private String idAnuncio, tituloAnuncio, sectorAnuncio, precioAnuncio, provinciaAnuncio, descripcionAnuncio;
    private TextView textDetalle;
    private EditText detalleTituloAnuncio, detallePrecio, detalleDescripcion;

    private Spinner spinner_sectores;
    private Spinner spinner_provincias;
    private String[] sectores = {"Seleccionar","Agricultura", "Minería", "Siderurgia", "Ganadería", "Pesca", "Construcción", "Industrías de procesado", "Fabricación",
            "Hostelería", "Mantenimiento", "Sanitario", "Transporte", "Investigación", "Administraciones", "Ingenieros"};

    private String[] provincias = {"Seleccionar", "Álava", "Albacete","Alicante", "Almería", "Madrid"};

    private Button modificarDatos, eliminarAnuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_anuncio);

        Bundle bundle = getIntent().getExtras();
        setAnuncio(bundle);

        textDetalle = (TextView)findViewById(R.id.tituloDetalle);
        textDetalle.setText(R.string.tituloDetalleAnuncio);

        detalleTituloAnuncio = (EditText) findViewById(R.id.detalleTituloAnuncio);
        detalleTituloAnuncio.setText(this.tituloAnuncio);

        setAdapterSpinnerSectores(bundle);
        setAdapterSpinnerProvincias(bundle);

        detallePrecio = (EditText) findViewById(R.id.detallePrecioAnuncio);
        detallePrecio.setText(this.precioAnuncio);

        detalleDescripcion = (EditText)findViewById(R.id.detalleTextoAnuncio);
        detalleDescripcion.setText(this.descripcionAnuncio);

        eliminarAnuncio = (Button)findViewById(R.id.buttonEliminarAnuncio);
        eliminarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });

        modificarDatos = (Button)findViewById(R.id.buttonModificarDatos);
        modificarDatos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                confirmModify();
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

    private void setAnuncio(Bundle bundle) {

        this.usuario_loggeado = (User)bundle.getSerializable("userLogged");
        this.idAnuncio = bundle.getString("idAnuncio");
        this.tituloAnuncio = bundle.getString("tituloAnuncio");
        this.sectorAnuncio = bundle.getString("sectorAnuncio");
        this.provinciaAnuncio = bundle.getString("provinciaAnuncio");
        this.precioAnuncio = bundle.getString("precioAnuncio");
        this.descripcionAnuncio = bundle.getString("descripcionAnuncio");
    }

    private void setAdapterSpinnerSectores(Bundle bundle) {

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sectores);
        spinner_sectores = (Spinner)findViewById(R.id.detalleSectorProfesional);
        spinner_sectores.setAdapter(adapter);

        int spinnerPositionSectores = adapter.getPosition(this.sectorAnuncio);
        spinner_sectores.setSelection(spinnerPositionSectores);
    }

    private void setAdapterSpinnerProvincias(Bundle bundle) {

        ArrayAdapter adapterProvincias = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provincias);
        spinner_provincias = (Spinner)findViewById(R.id.detalleProvincia);
        spinner_provincias.setAdapter(adapterProvincias);

        int spinnerPositionProvincias = adapterProvincias.getPosition(this.provinciaAnuncio);
        spinner_provincias.setSelection(spinnerPositionProvincias);
    }

    private void confirmDelete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleAnuncioActivity.this);

        builder.setMessage(R.string.deleteMessage)
                .setTitle(R.string.confirmTitle)
                .setPositiveButton(R.string.optionYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAnuncio(idAnuncio);
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

    private void confirmModify() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleAnuncioActivity.this);

        builder.setMessage(R.string.modifyMessage)
                .setTitle(R.string.confirmTitle)
                .setPositiveButton(R.string.optionYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                modify(idAnuncio);
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

    private void deleteAnuncio(String idAnuncio) {

        new AsyncLogin().execute("delete", idAnuncio);
    }

    private void modify(String idAnuncio) {

        String modifyTitulo = detalleTituloAnuncio.getText().toString();
        String modifySector = spinner_sectores.getSelectedItem().toString();
        String modifyProvincia = spinner_provincias.getSelectedItem().toString();
        String modifyPrecio = detallePrecio.getText().toString();
        String modifyDesc = detalleDescripcion.getText().toString();

        new AsyncLogin().execute("modify", idAnuncio,modifyTitulo, modifySector, modifyProvincia, modifyPrecio, modifyDesc);
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(DetalleAnuncioActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Enviando datos...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://10.0.2.2:80/api/v1/anuncio/"+Integer.parseInt(params[1]));

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }

            try  {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod((params[0] == "delete") ? "DELETE" : "PUT");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                if(params[0] == "modify") {

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("titulo", params[2])
                            .appendQueryParameter("sector_profesional", params[3])
                            .appendQueryParameter("provincia", params[4])
                            .appendQueryParameter("precio_maximo", params[5])
                            .appendQueryParameter("descripcion", params[6]);

                    String query = builder.build().getEncodedQuery();
                    writer.write(query);
                }

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

                    Toast.makeText(DetalleAnuncioActivity.this, R.string.mensaje_accion_ok, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    i.putExtra("user", usuario_loggeado);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);

                } else {
                    Toast.makeText(DetalleAnuncioActivity.this, R.string.mensaje_accion_error, Toast.LENGTH_LONG).show();
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
