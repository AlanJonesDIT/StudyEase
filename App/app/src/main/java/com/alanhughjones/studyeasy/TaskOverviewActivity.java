package com.alanhughjones.studyeasy;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskOverviewActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    public int taskCnt;
    public int taskCntO;

    private String selectedSubject;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);
        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_task_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView subjectTitle = findViewById(R.id.overview_subj);
        Button btnAddTask = findViewById(R.id.add_task_done);
        TextView overDue = findViewById(R.id.overdue_count);
        TextView notOverdue = findViewById(R.id.task_count);
        myDB = new DatabaseHelper(this);
        taskCnt = 0;
        taskCntO = 0;

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTask = new Intent(TaskOverviewActivity.this,NewTaskActivity.class);
                newTask.putExtra("id",selectedID);
                newTask.putExtra("name",selectedSubject);
                startActivity(newTask);
            }
        });

        // get the intent extra from the SubjectListActivity
        Intent receivedIntent = getIntent();
        //now get the subjectID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1);
        //now get the name we passed as an extra
        selectedSubject = receivedIntent.getStringExtra("name");

        //set the text to show the current selected subject
        subjectTitle.setText(selectedSubject);
        ListView taskListView = (ListView) findViewById(R.id.task_list);
        List<Task> mProductList = new ArrayList<>();
        Cursor tasks = myDB.getTaskData(selectedID);




        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        if (tasks.getCount() == 0){
            return;
        } else {
            while (tasks.moveToNext()) {
                mProductList.add(new Task(tasks.getInt(0), tasks.getString(1), tasks.getString(2)));
                Date strDate;
                try {
                    strDate = sdf.parse(tasks.getString(2));
                    if (new Date().after(strDate)){
                        taskCntO += 1;
                    } else {
                        taskCnt +=1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            //Set count total
            //overDue.setText(Integer.toString(taskCntO));
            overDue.setText(Integer.toString(taskCntO));
            notOverdue.setText(Integer.toString(taskCnt));

            // Sort arrayList
            // #############
        }
        //Init adapter
        TaskListAdapter adapter = new TaskListAdapter(getApplicationContext(), mProductList);
        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent editTask = new Intent(TaskOverviewActivity.this,EditTaskActivity.class);
                Object taskID = view.getTag();
                editTask.putExtra("taskID",taskID.toString());
                editTask.putExtra("subID",selectedID);
                editTask.putExtra("name",selectedSubject);
                startActivity(editTask);
                Toast.makeText(getApplicationContext(), "Task ID = " + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Comparator for Descending Date
}
