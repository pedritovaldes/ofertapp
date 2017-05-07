package com.example.pedro.ofertapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
public class DetalleAnuncio extends Activity {

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

    private String[] provincias = {"Seleccionar", "Álava", "Albaecete","Alicante", "Almería", "Madrid"};

    private Button modificarDatos, eliminarAnuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleAnuncio.this);

        builder.setMessage(R.string.deleteMessage)
                .setTitle(R.string.deleteConfirmTitle)
                .setPositiveButton(R.string.deleteOptionYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAnuncio(idAnuncio);
                            }
                        })
                .setNegativeButton(R.string.deleteOptionCancel,
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

        new AsyncLogin().execute(Integer.parseInt(idAnuncio));
    }

    private class AsyncLogin extends AsyncTask<Integer, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(DetalleAnuncio.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Eliminando...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(Integer... params) {

            try {
                url = new URL("http://10.0.2.2:80/api/v1/anuncio/"+params[0]);

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

                    Toast.makeText(DetalleAnuncio.this, R.string.mensaje_ok_delete_anuncio, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    i.putExtra("user", usuario_loggeado);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);

                } else {
                    Toast.makeText(DetalleAnuncio.this, R.string.mensaje_error_delete_anuncio, Toast.LENGTH_LONG).show();
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
