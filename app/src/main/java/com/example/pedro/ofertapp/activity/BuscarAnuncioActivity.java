package com.example.pedro.ofertapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pedro.ofertapp.DatePickerFragment;
import com.example.pedro.ofertapp.R;
import com.example.pedro.ofertapp.model.User;

/**
 * Created by Pedro on 18/06/2017.
 */
public class BuscarAnuncioActivity extends AppCompatActivity{

    private User usuario_loggeado;
    private Spinner spinner_sectores;
    private Spinner spinner_provincias;

    private String[] sectores = {"Seleccionar","Agricultura", "Minería", "Siderurgia", "Ganadería", "Pesca", "Construcción", "Industrías de procesado", "Fabricación",
            "Hostelería", "Mantenimiento", "Sanitario", "Transporte", "Investigación", "Administraciones", "Ingenieros"};

    private String[] provincias = {"Seleccionar", "Álava", "Albaecete","Alicante", "Almería"};

    private Spinner sectorProfesional, selectedProvincia;
    private EditText precio_max;
    private TextView fecha;
    private Button buscarAnuncio;

    public void onCreate(Bundle savedInstanceState) {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_anuncio);

        this.usuario_loggeado = (User)getIntent().getExtras().getSerializable("user");

        ArrayAdapter adapterSectores = new ArrayAdapter<String>(this, R.layout.spinner_item, sectores);
        spinner_sectores = (Spinner)findViewById(R.id.buscar_anuncio_sector);
        spinner_sectores.setAdapter(adapterSectores);

        ArrayAdapter adapterProvincia = new ArrayAdapter<String>(this, R.layout.spinner_item, provincias);
        spinner_provincias = (Spinner)findViewById(R.id.buscar_anuncio_provincia);
        spinner_provincias.setAdapter(adapterProvincia);

        setColorSpinner(spinner_sectores);
        setColorSpinner(spinner_provincias);

        sectorProfesional = (Spinner)findViewById(R.id.buscar_anuncio_sector);
        selectedProvincia = (Spinner)findViewById(R.id.buscar_anuncio_provincia);
        precio_max = (EditText)findViewById(R.id.buscar_anuncio_precio_servicio);
        fecha = (TextView)findViewById(R.id.showDate);

        buscarAnuncio = (Button)findViewById(R.id.button_buscar_anuncio);
        buscarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarListadoAnuncios();
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
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent= new Intent();
                intent.putExtra("user", usuario_loggeado);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case R.id.menu_logout:
                confirmLogout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmLogout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BuscarAnuncioActivity.this);

        builder.setMessage(R.string.logoutMessage)
                .setTitle(R.string.confirmTitle)
                .setPositiveButton(R.string.optionYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(BuscarAnuncioActivity.this, LoginActivity.class)
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

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void lanzarListadoAnuncios() {
        Intent i = new Intent(this, ListadoAnunciosActivity.class);
        i.putExtra("user", usuario_loggeado);
        i.putExtra("sector", sectorProfesional.getSelectedItem().toString());
        i.putExtra("provincia", selectedProvincia.getSelectedItem().toString());
        i.putExtra("precio", precio_max.getText().toString());
        i.putExtra("fecha", fecha.getText().toString());
        startActivity(i);
    }

    private void setColorSpinner(Spinner spinner) {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

}
