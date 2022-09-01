package com.app.jpf.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.app.jpf.R;
import com.app.jpf.databinding.SpinnerRowBinding;
import com.app.jpf.models.AreaModel;

import java.util.List;

import io.paperdb.Paper;

public class SpinnerAreaAdapter extends BaseAdapter {
    private List<AreaModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;

    public SpinnerAreaAdapter(List<AreaModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row,parent,false);
       String title;
        if(lang.equals("ar")) {
             title = list.get(position).getCity_name_ar();
        }
        else {
            title=list.get(position).getCity_name_en();
        }
        binding.setTitle(title);
        return binding.getRoot();
    }
}
