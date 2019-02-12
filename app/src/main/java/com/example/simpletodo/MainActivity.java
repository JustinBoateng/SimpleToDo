package com.example.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;  //ArrayList, not String[]
    ArrayAdapter<String> itemsAdapter; //wires the model to the view
    ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems  =  (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        //mock data
        //items.add("First Item");
        //items.add("Second item");

        setupListViewListener();
    }

    public void onAddItem(View v){ //Summoned in 'Add' Button script.
        EditText etNewItem = (EditText) findViewById(R.id.etnewitem);
        String itemText = etNewItem.getText().toString(); //read input from user
        itemsAdapter.add(itemText); //wire input from string object to ArrayAdapter
        etNewItem.setText("");

        writeItems();

        Toast.makeText(getApplicationContext(),"Item added to list", Toast.LENGTH_SHORT).show();
        //Toasts are little notifications telling the user that the process was successful
        //(Application Context in which the notification refers to, text to display to the user, duration for which the notification would be displayed)
    }

    private void setupListViewListener(){
        Log.i("MainActivity","Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               Log.i("MainActivity", "Item removed from list: " + position);
               items.remove(position);
               itemsAdapter.notifyDataSetChanged();

               writeItems();

               return true; //the long click needs to be consumed(confirmed) so the return needs to be true, not false
            }
        });
    }


    ///methods to support persistence
    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }


    //methods to read/write to the todo.txt file

    private void readItems(){ //summoned at the very beginning
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch(Exception e){
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>(); //initialize here just to make sure that the object is substantiated correctly and that we dont end up with a NullPointerException
            //in fact, if you get a FileNotFound Exception, it's because this is the first time the app has been run without the todo.txt file.
            //running it again with the data saved eliminates the error
        }
     }

    private void writeItems(){ //summoned when adding or removing from the list
        try{
            FileUtils.writeLines(getDataFile(), items);
        } catch(Exception e){
            Log.e("MainActivity", "Error writing file", e);
        }

    }
}

