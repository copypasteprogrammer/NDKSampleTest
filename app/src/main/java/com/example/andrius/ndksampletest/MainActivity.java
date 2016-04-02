package com.example.andrius.ndksampletest;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Button;
import android.view.View.OnClickListener;

import android.os.AsyncTask;
import android.util.Log;
import android.content.res.AssetManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import android.os.Environment;


public class MainActivity extends Activity implements OnClickListener {
    static {
        System.loadLibrary("GTTknP11");

        System.loadLibrary("ndkLib");
        //System.loadLibrary("nkdLib");
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    private String caCertificateName;
    private  String clientCertificateName;
    private  String clientCertificatePassword = "CLslaptazodis";
    private  String exampleUrl;
    private TextView  mainTextView;
    private Api exampleApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = (TextView) findViewById(R.id.my_textview);

        t.setText(callNative());
        Button btn1 = (Button)findViewById(R.id.button1);
        Button btn2 = (Button)findViewById(R.id.button2);
        Button btn3 = (Button)findViewById(R.id.button3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        caCertificateName = getResources().getString(R.string.server_cert_asset_name);
        clientCertificateName = getResources().getString(R.string.client_cert_file_name);
        exampleUrl = getResources().getString(R.string.example_url);
        mainTextView = (TextView) findViewById(R.id.my_textview4);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        TextView text2 = (TextView) findViewById(R.id.my_textview2);
        TextView text3 = (TextView) findViewById(R.id.my_textview3);

        switch (v.getId()) {
            case R.id.button1:
                Intent intent  = new Intent(this,Activity2.class );
                startActivity(intent);
                break;
            case R.id.button2:
                text3.setText(callNative2("mesagree"));
                break;
            case R.id.button3:
                doRequest();
                break;
        }
    }
    public native String callNative();

    public native String callNative2(String msg);

    public  native double myMethod( String path);






    private void updateOutput(String text) {

        mainTextView.setText(mainTextView.getText() + "\n\n" + text);
    }

    private void doRequest() {

        try {
            AuthenticationParameters authParams = new AuthenticationParameters();
            authParams.setClientCertificate(getClientCertFile());
            authParams.setClientCertificatePassword(clientCertificatePassword);
            authParams.setCaCertificate(readCaCert());

            exampleApi = new Api(authParams);

            updateOutput("Connecting to " + exampleUrl);

            new AsyncTask() {
                @Override
                protected Object doInBackground(Object... objects) {

                    try {
                        String result = exampleApi.doGet(exampleUrl);
                        int responseCode = exampleApi.getLastResponseCode();
                        if (responseCode == 200) {
                            publishProgress(result);
                        } else {
                            publishProgress("HTTP Response Code: " + result);
                        }

                    } catch (Throwable ex) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PrintWriter writer = new PrintWriter(baos);
                        ex.printStackTrace(writer);
                        writer.flush();
                        writer.close();
                        publishProgress(ex.toString() + " : " + baos.toString());
                    }

                    return null;
                }

                @Override
                protected void onProgressUpdate(final Object... values) {
                    StringBuilder buf = new StringBuilder();
                    for (final Object value : values) {
                        buf.append(value.toString());
                    }
                    updateOutput(buf.toString());
                }

                @Override
                protected void onPostExecute(final Object result) {
                    updateOutput("Done!");
                }
            }.execute();

        } catch (Exception ex) {
            Log.e(TAG, "failed to create timeApi", ex);
            updateOutput(ex.toString());
        }
    }



    private File getClientCertFile() {

        File externalStorageDir = Environment.getExternalStorageDirectory();
        return new File(externalStorageDir, clientCertificateName);
    }

    private String readCaCert() throws Exception {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open(caCertificateName);
        return IOUtil.readFully(inputStream);
    }

}
