package com.example.pedro.ofertapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedro.ofertapp.R;
import com.example.pedro.ofertapp.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Pedro on 29/11/2016.
 */
public class LoginActivity extends AppCompatActivity{

    private TextView link_registro;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    private Client client;
    private WebTarget target;

    private EditText etEmail;
    private EditText etPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.login);

        link_registro = (TextView)findViewById(R.id.link_registro);
        link_registro.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });

        etEmail = (EditText)findViewById(R.id.login_user);
        etPassword = (EditText)findViewById(R.id.login_pass);
    }

    public void lanzarRegistro(View view) {
        Intent i = new Intent(this, RegistroActivity.class);
        startActivity(i);
    }

    public void login(View view) throws JSONException {

        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        new AsyncLoginJerseyClient().execute(email, pass);
    }

    private class AsyncLoginJerseyClient extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Comprobando...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            client = ClientBuilder.newClient();
            target = client.target(getString(R.string.emulatorDeviceEndpoint));


            JSONObject json = new JSONObject();
            try {
                json.put("email", params[0]);
                json.put("password", params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String input = json.toString();
            target = target.path("login");

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(input, MediaType.APPLICATION_JSON),Response.class);

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
            Integer id;
            String name, email, telefono, pass, descripcion;

            try {

                JSONObject obj = new JSONObject(result);
                code = obj.getInt("code");

                if(code.equals(200)) {

                    id = obj.getInt("user_id");
                    name = obj.getString("user_nombre");
                    email = obj.getString("user_email");
                    telefono = obj.getString("user_telefono");
                    pass = obj.getString("user_pass");
                    descripcion = obj.getString("user_desc");

                    User user= new User(id, name, email, telefono, descripcion, pass);
                    Toast.makeText(LoginActivity.this, R.string.mensaje_ok_login, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    i.putExtra("user", user);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);

                } else if(code.equals(400)) {

                    Toast.makeText(LoginActivity.this, R.string.mensaje_error_login_user, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.mensaje_error_login, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
