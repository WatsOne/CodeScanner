package org.kulikov.codescanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

public class SavedResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_results_view);

        settingAdapter();
    }

    private void settingAdapter() {
        SavedResultsListAdapter adapter = new SavedResultsListAdapter(this);
        List<BarCodeEntity> savedBarResultsList = BarCodeEntity.listAll(BarCodeEntity.class);
        adapter.setData(savedBarResultsList);

        ListView savedResultListView = (ListView) findViewById(R.id.saved_result_list_view);
        savedResultListView.setAdapter(adapter);
    }
}
