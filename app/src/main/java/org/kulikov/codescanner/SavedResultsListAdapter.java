package org.kulikov.codescanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SavedResultsListAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<BarCodeEntity> list;

    public SavedResultsListAdapter(Context context) {
        myInflater = LayoutInflater.from(context);
    }

    public void setData(List<BarCodeEntity> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView  = myInflater.inflate(R.layout.saved_results_row, null);
        TextView savedResultTextView = (TextView) convertView.findViewById(R.id.saved_result);
        convertView.setTag(savedResultTextView);
        savedResultTextView.setText(list.get(position).getBarCodeResult());

        return convertView;
    }
}