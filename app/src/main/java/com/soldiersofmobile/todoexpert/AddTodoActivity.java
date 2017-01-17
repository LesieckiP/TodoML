package com.soldiersofmobile.todoexpert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddTodoActivity extends AppCompatActivity {

    @BindView(R.id.content_edit_text) EditText contentEditText;
    @BindView(R.id.done_check_box) CheckBox doneCheckBox;
    @BindView(R.id.add_button) Button addButton;
    @BindView(R.id.activity_add_todo) RelativeLayout activityAddTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(TodoListActivity.TODO_EXTRA)) {
            Todo todo = (Todo) getIntent().getParcelableExtra(TodoListActivity.TODO_EXTRA);
            contentEditText.setText(todo.getContent());
            doneCheckBox.setChecked(todo.isDone());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.add_button)
    public void onClick() {
        Todo todo = new Todo(contentEditText.getText().toString(), doneCheckBox.isChecked());

        Intent intent = new Intent();
        intent.putExtra(TodoListActivity.TODO_EXTRA, todo);
        setResult(RESULT_OK, intent);
        finish();
    }
}
