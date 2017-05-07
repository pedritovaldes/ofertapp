package com.example.pedro.ofertapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Pedro on 06/05/2017.
 */
public class DetalleAnuncio extends Activity {

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

        this.usuario_loggeado = (User)getIntent().getExtras().getSerializable("user");

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

    }
}
