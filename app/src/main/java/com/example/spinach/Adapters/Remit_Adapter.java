package com.example.spinach.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.spinach.R;
import com.example.spinach.Remit_Item;

import java.util.List;

import javax.security.auth.Subject;

public class Remit_Adapter extends BaseAdapter {
    Context context;
    List<Remit_Item> TempSubjectList;
    public Remit_Adapter(List<Remit_Item> listValue, Context context) {
        this.context = context;
        this.TempSubjectList = listValue;
    }
    @Override
    public int getCount() { return this.TempSubjectList.size();
    }
    @Override
    public Object getItem(int position) {
        return this.TempSubjectList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewItem viewItem = null;
        if(convertView == null) {
            viewItem = new ViewItem();
            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInfiater.inflate(R.layout.remit_listview, null);
            viewItem.IdTextView = (TextView)convertView.findViewById(R.id.textViewDate);
            viewItem.NameTextView = (TextView)convertView.findViewById(R.id.textviewAmount);
            convertView.setTag(viewItem);
        } else {
            viewItem = (ViewItem) convertView.getTag();
        }
        viewItem.IdTextView.setText(TempSubjectList.get(position).amount);
        viewItem.NameTextView.setText(TempSubjectList.get(position).date);
        return convertView;
    }
}
class ViewItem {
    TextView IdTextView;
    TextView NameTextView;
}
