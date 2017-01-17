package com.soldiersofmobile.todoexpert;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.soldiersofmobile.todoexpert.api.TodoItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodosAdapter extends BaseAdapter {

    private List<TodoItem> todos = new ArrayList<>();

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public TodoItem getItem(int position) {
        return todos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("TAG", String.format("pos %d cv:%s", position, convertView));

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        TodoItem todoFromApi = getItem(position);

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.todoCheckBox.setText(todoFromApi.content);
        viewHolder.todoCheckBox.setChecked(todoFromApi.done);
        viewHolder.idTextView.setText(todoFromApi.objectId);

        return view;
    }

    public void addAll(List<TodoItem> results) {
        todos.clear();
        todos.addAll(results);
        notifyDataSetChanged();
    }

    public void add(TodoItem todo) {

        todos.add(todo);
        notifyDataSetChanged();

    }

    static class ViewHolder {
        @BindView(R.id.todoCheckBox)
        CheckBox todoCheckBox;
        @BindView(R.id.idTextView)
        TextView idTextView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
