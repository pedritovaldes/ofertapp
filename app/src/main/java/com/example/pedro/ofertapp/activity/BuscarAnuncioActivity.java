package com.example.pedro.ofertapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pedro.ofertapp.R;
import com.example.pedro.ofertapp.model.User;

/**
 * Created by Pedro on 18/06/2017.
 */
public class BuscarAnuncioActivity extends AppCompatActivity {

    private User usuario_loggeado;
    private Spinner spinner_sectores;
    private Spinner spinner_provincias;

    private String[] sectores = {"Seleccionar","Agricultura", "Minería", "Siderurgia", "Ganadería", "Pesca", "Construcción", "Industrías de procesado", "Fabricación",
            "Hostelería", "Mantenimiento", "Sanitario", "Transporte", "Investigación", "Administraciones", "Ingenieros"};

    private String[] provincias = {"Seleccionar", "Álava", "Albaecete","Alicante", "Almería"};

    private Spinner sectorProfesional, selectedProvincia;
    private CheckBox horas, dias, mes, noFecha;
    private EditText precio_max;

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
        horas = (CheckBox)findViewById(R.id.checkBoxFecha24Horas);
        dias = (CheckBox)findViewById(R.id.checkBoxFecha7Dias);
        mes = (CheckBox)findViewById(R.id.checkBoxFechaMes);
        noFecha = (CheckBox)findViewById(R.id.checkBoxFechaSinFecha);

        horas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v, horas, dias, mes, noFecha);
            }
        });

        dias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v, horas, dias, mes, noFecha);
            }
        });

        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v, horas, dias, mes, noFecha);
            }
        });

        noFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v, horas, dias, mes, noFecha);
            }
        });
    }

    private void onCheckboxClicked(View v, CheckBox horas, CheckBox dias, CheckBox mes, CheckBox noFecha) {

        switch (v.getId()) {
            case R.id.checkBoxFecha24Horas:
                if (horas.isChecked()) {
                    dias.setChecked(false);
                    mes.setChecked(false);
                    noFecha.setChecked(false);
                }
                break;
            case R.id.checkBoxFecha7Dias:
                if(dias.isChecked()) {
                    horas.setChecked(false);
                    mes.setChecked(false);
                    noFecha.setChecked(false);
                }
                break;
            case R.id.checkBoxFechaMes:
                if(mes.isChecked()) {
                    horas.setChecked(false);
                    dias.setChecked(false);
                    noFecha.setChecked(false);
                }
                break;
            case R.id.checkBoxFechaSinFecha:
                if(noFecha.isChecked()){
                    horas.setChecked(false);
                    dias.setChecked(false);
                    mes.setChecked(false);
                }
                break;
            default:
                horas.setChecked(true);
                dias.setChecked(true);
                mes.setChecked(true);
                noFecha.setChecked(true);
        }
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
