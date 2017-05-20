package com.example.pedro.ofertapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.pedro.ofertapp.model.Anuncio;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Pedro on 01/05/2017.
 */
public class LoadJSONTask extends AsyncTask<String, Void, ResponseAnuncio> {

    private Context mContext;
    private ProgressDialog loadingDialog;

    private Listener mListener;

    public LoadJSONTask(Listener listener/*, Context context*/) {

        mListener = listener;
        //this.mContext = context;
    }

    public interface Listener {

        void onLoaded(List<Anuncio> androidList);

        void onError();
    }

    /*@Override
    protected void onPreExecute()
    {
        loadingDialog = new ProgressDialog(mContext);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //loadingDialog.setMessage(getString(R.string.dialog_message_sending_user_data));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        super.onPreExecute();
    }*/

    @Override
    protected ResponseAnuncio doInBackground(String... strings) {
        try {

            String stringResponse = loadJSON(strings[0]);
            Gson gson = new Gson();

            return gson.fromJson(stringResponse, ResponseAnuncio.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ResponseAnuncio response) {

        //loadingDialog.dismiss();

        if (response != null) {

            mListener.onLoaded(response.getAnuncios());

        } else {

            mListener.onError();
        }
    }

    private String loadJSON(String jsonURL) throws IOException {

        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = in.readLine()) != null) {

            response.append(line);
        }

        in.close();
        return response.toString();
    }
}
