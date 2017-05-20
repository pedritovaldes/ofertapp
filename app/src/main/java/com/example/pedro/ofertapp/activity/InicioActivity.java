package com.example.pedro.ofertapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pedro.ofertapp.R;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }

    public void lanzarRegistro(View vire) {
        Intent i = new Intent(this, RegistroActivity.class);
        startActivity(i);
    }

    public void lanzarLogin(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
