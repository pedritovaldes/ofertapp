package com.example.pedro.ofertapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;

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

        tituloAnuncioTextView = (TextView)findViewById(R.id.detalleListadoTitulo);
        tituloAnuncioTextView.setText(tituloAnuncio);

        provinciaAnuncioTextView = (TextView)findViewById(R.id.detalleListadoProvincia);
        provinciaAnuncioTextView.setText(provinciaAnuncio);

        sectorAnuncioTextView = (TextView)findViewById(R.id.detalleListadoSector);
        sectorAnuncioTextView.setText(sectorAnuncio);

        precioAnuncioTextView = (TextView)findViewById(R.id.detalleListadoPrecio);
        precioAnuncioTextView.setText(precioAnuncio);

        descripcionAnuncioTextView = (TextView)findViewById(R.id.detalleListadoDescripcion);
        descripcionAnuncioTextView.setText(descripcionAnuncio);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(ListadoDetalleAnuncioActivity.this);

        builder.setMessage(R.string.logoutMessage)
                .setTitle(R.string.confirmTitle)
                .setPositiveButton(R.string.optionYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(ListadoDetalleAnuncioActivity.this, LoginActivity.class)
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
