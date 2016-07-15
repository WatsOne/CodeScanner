package org.kulikov.codescanner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private final String RESULT_WAS_GIVEN_FLAG = "RESULT_WAS_GIVEN";

    private TextView resultTextView;
    private TextView resultLabel;
    private Button sendButton;
    private Button saveButton;

    private Context context;
    private String previousSavedResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resultTextView = (TextView) findViewById(R.id.result);
        resultLabel = (TextView) findViewById(R.id.result_label);
        sendButton = (Button) findViewById(R.id.send_btn);
        saveButton = (Button) findViewById(R.id.save_btn);

        if (savedInstanceState != null && savedInstanceState.getBoolean(RESULT_WAS_GIVEN_FLAG)) {
            updateControlsAfterGettingResult();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RESULT_WAS_GIVEN_FLAG, resultTextView.getText().length() > 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {

            showAboutToast();
            return true;

        } else if (id == R.id.action_view_saved) {

            startActivity(new Intent(this, SavedResultsActivity.class));
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onStartScannerClick(View view) {

        if (!isCameraAvailable()) {
            Toast.makeText(this, "На устройстве не обнаружена камера", Toast.LENGTH_LONG).show();
            return;
        }

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    public void onSendClick(View view) {
        sendScannedResult();
    }

    public void onSaveClick(View view) {
        saveScannedResult();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() == null) {
                Log.d(AppConstants.TAG, "Cancelled scan");
                Toast.makeText(this, "Сканирование отменено", Toast.LENGTH_LONG).show();
            } else {
                Log.d(AppConstants.TAG, "Scanned");

                resultTextView.setText(result.getContents());
                updateControlsAfterGettingResult();
            }
        } else {
            Log.d(AppConstants.TAG, "Weird");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateControlsAfterGettingResult() {
        resultLabel.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
    }

    private void showAboutToast() {
        Toast aboutToast = Toast.makeText(this, "Сканнер штрих кодов. Тестовое задание. Куликов А.С. ©2015", Toast.LENGTH_LONG);
        aboutToast.setGravity(Gravity.CENTER, 0, 0);
        aboutToast.show();
    }

    private void sendScannedResult() {
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(this);
        asyncHttpPost.execute(resultTextView.getText().toString());
    }

    private boolean isCameraAvailable() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void saveScannedResult(){
        String result = resultTextView.getText().toString();

        if (previousSavedResult != null && previousSavedResult.equals(result)) {
            Toast.makeText(this, "Текущий результат уже сохранен!", Toast.LENGTH_LONG).show();
            return;
        }

        BarCodeEntity entity = new BarCodeEntity(result);
        entity.save();

        Log.d(AppConstants.TAG, "Result saved into DB");
        Toast.makeText(this, "Результаты сохранены", Toast.LENGTH_LONG).show();
        previousSavedResult = result;
    }

}
