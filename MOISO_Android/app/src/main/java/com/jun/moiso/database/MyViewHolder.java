package com.jun.moiso.database;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder <T extends ViewDataBinding> extends RecyclerView.ViewHolder {


    private final T binding;

    public MyViewHolder(final View v){
        super(v);
        this.binding = (T) DataBindingUtil.bind(v);

    }

    public T binding() {
        return binding;
    }
}
