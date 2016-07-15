package org.kulikov.codescanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;


public class AsyncHttpPost extends AsyncTask<String,String,String>
{
    private Activity source;
    private ProgressDialog progressDialog;

    public AsyncHttpPost(Activity source) {
        this.source = source;
        progressDialog = new ProgressDialog(source);
    }

    @Override
    protected void onPreExecute() {
        this.progressDialog.setMessage("Отправка результата");
        this.progressDialog.show();
        Log.d(AppConstants.TAG, "Sending");
    }

    @Override
    protected String doInBackground(String... params) {

        BufferedWriter writer = null;
        OutputStream outputStream = null;

        try {
            URL url = new URL(AppConstants.SENDING_RESULTS_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            outputStream = conn.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            StringBuilder paramBuilder = new StringBuilder();
            paramBuilder.append(URLEncoder.encode(AppConstants.RESULT_POST_PARAM_NAME, "UTF-8"));
            paramBuilder.append("=");
            paramBuilder.append(URLEncoder.encode(params[0], "UTF-8"));

            writer.write(paramBuilder.toString());
            writer.flush();

            int responseCode = conn.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                Toast.makeText(source, "При отправке результата произошла ошибка", Toast.LENGTH_LONG).show();
                Log.d(AppConstants.TAG, "Send fail. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignored) {

                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {

                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
            Toast.makeText(source, "Результат успешно отправлен", Toast.LENGTH_LONG).show();
            Log.d(AppConstants.TAG, "Sent successful");
        }
    }
}
