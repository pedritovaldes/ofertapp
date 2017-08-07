package com.example.pedro.ofertapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pedro.ofertapp.R;
import com.example.pedro.ofertapp.model.User;

/**
 * Created by Pedro on 27/07/2017.
 */
public class ListadoDetalleAnuncioActivity extends AppCompatActivity {

    private User usuario_loggeado;
    private String idAnuncio, tituloAnuncio, sectorAnuncio, precioAnuncio, provinciaAnuncio, descripcionAnuncio, nombreUsuario, emailUsuario,
    telefonoUsuario;

    private TextView nombreUsuarioTextView, emailUsuarioTextView, telefonoUsuarioTextView, tituloAnuncioTextView, sectorAnuncioTextView,
    precioAnuncioTextView, provinciaAnuncioTextView, descripcionAnuncioTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_detalle_anuncio);

        Bundle bundle = getIntent().getExtras();
        getAnuncio(bundle);

        nombreUsuarioTextView = (TextView)findViewById(R.id.nombreUsuario);
        nombreUsuarioTextView.setText(nombreUsuario);

        emailUsuarioTextView = (TextView)findViewById(R.id.emailUsuario);
        emailUsuarioTextView.setText(emailUsuario);

        telefonoUsuarioTextView = (TextView)findViewById(R.id.telefonoUsuario);
        telefonoUsuarioTextView.setText(telefonoUsuario);

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

    private void getAnuncio(Bundle bundle) {

        this.usuario_loggeado = (User)bundle.getSerializable("userLogged");
        this.idAnuncio = bundle.getString("idAnuncio");
        this.tituloAnuncio = bundle.getString("tituloAnuncio");
        this.sectorAnuncio = bundle.getString("sectorAnuncio");
        this.provinciaAnuncio = bundle.getString("provinciaAnuncio");
        this.precioAnuncio = bundle.getString("precioAnuncio");
        this.descripcionAnuncio = bundle.getString("descripcionAnuncio");
        this.nombreUsuario = bundle.getString("nombreUsuario");
        this.emailUsuario = bundle.getString("emailUsuario");
        this.telefonoUsuario = bundle.getString("telefonoUsuario");
    }
}
