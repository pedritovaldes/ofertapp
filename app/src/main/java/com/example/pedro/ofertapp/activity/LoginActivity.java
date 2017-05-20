package com.example.pedro.ofertapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Pedro on 29/11/2016.
 */
public class LoginActivity extends Activity {

    private TextView link_registro;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

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

    public void login(View view) {

        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        new AsyncLogin().execute(email, pass);

        //Client client = ClientBuilder.newClient();
        //WebTarget t = client.target("http://aosgc-board.herokuapp.com/board/message");
        /*Client c = ClientBuilder.newClient()
                .target("http://aosgc-board.herokuapp.com/board/message")
                .path("path")
                .queryParam().request().get();*/
        //WebTarget t = c.target("http://aosgc-board.herokuapp.com/board/message");

        //String s = t.request().accept(MediaType.APPLICATION_JSON_TYPE).get(Response.class).toString();
        //System.out.println(s);
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Comprobando...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://10.0.2.2:80/api/v1/login");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            }

            try  {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //Agregamos parametros a la URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[0])
                        .appendQueryParameter("password", params[1]);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
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

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
