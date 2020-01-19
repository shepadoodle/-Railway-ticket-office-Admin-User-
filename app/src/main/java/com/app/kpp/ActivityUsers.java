package com.app.kpp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.kpp.Adapters.TrainListAdapter;

import com.app.kpp.Models.Train;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static java.io.File.separator;

public class ActivityUsers extends AppCompatActivity implements View.OnClickListener {

    @TargetApi(Build.VERSION_CODES.KITKAT)

    FirebaseDatabase dbU;
    DatabaseReference trains;//работа с таблицами
    DatabaseReference find_trains_tab;//работа с таблицами
    DatabaseReference users;//работа с таблицами


    EditText et_user1, et_user2;
    Button selectDate1, selectDate2,find_train,btnChecked;
    TextView date1, date2, csv_tv;
    DatePickerDialog datePickerDialog1,datePickerDialog2;
    ListView lv_select_trains;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;



    private String TAG = "myLogs";

    ArrayList<Train> find_train_List = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        setTitle("Users Activity");

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        File newFile = new File(path + "/newfile1.csv");
        File logFile = new File(path + "/Log.txt");

        csv_tv = findViewById(R.id.csv_tv);
        et_user1 = findViewById(R.id.et_user1);
        et_user2 = findViewById(R.id.et_user2);
        selectDate1 = findViewById(R.id.btnDate1);
        date1 = findViewById(R.id.tvSelectedDate1);
        selectDate2 = findViewById(R.id.btnDate2);
        date2 = findViewById(R.id.tvSelectedDate2);
        find_train= findViewById(R.id.find_train);
        selectDate1.setOnClickListener(this);
        selectDate2.setOnClickListener(this);

        dbU = FirebaseDatabase.getInstance();
        trains = dbU.getReference("Trains");
        find_trains_tab = dbU.getReference("Find Trains");
        users = dbU.getReference("Users");


        LayoutInflater inflater = LayoutInflater.from(this); //Получаем нужный шаблон
        View lv_inf = inflater.inflate(R.layout.lv, null);



        find_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appendLog("Log.info: User method onClick ",logFile);
                FireBaseDatabaseHelper helper = new FireBaseDatabaseHelper();
                helper.readTrains(new FireBaseDatabaseHelper.DataTrainStatus() {
                    @Override
                    public void dataTrainStatus(List<Train> trainsList, List<String> keys) {
                        //Iterator<Train> itr = trainsList.iterator();
                        for(final Train train :trainsList) {

                        //while (itr.hasNext()){
                          /*  Log.d(TAG, "findtr_btn: train.get - "+"\n" + train.getStarting_sration()+"\n"
                                    +"train.get - " + train.getEnd_station()+"\n"+
                                    "train.get - " + train.getDate_departure()+"\n"+
                                    "train.get - " + train.getDate_arival()+"\n"+
                                    "train.get - " + train.getTime_start()+"\n"+
                                    "train.get - " + train.getTime_end()+"\n"+
                                    "********************************************\n" +
                                    "train.et1 - " + et_user1.getText().toString()+"\n"+
                                    "train.et2 - " + et_user2.getText().toString()+"\n"+
                                    "train.date1 - " + date1.getText().toString()+"\n"+
                                    "train.date2 - " + date2.getText().toString()+"\n");*/
                            if (et_user1.getText().toString().equals(train.getStarting_sration())&&
                                et_user2.getText().toString().equals(train.getEnd_station())&&
                                date1.getText().toString().equals(train.getDate_departure())&&
                                date2.getText().toString().equals(train.getDate_arival()))
                            {
                                final Toast toast = Toast.makeText(getApplicationContext(),
                                        "Поезд найден",
                                        Toast.LENGTH_SHORT);
                                toast.show();


                                find_train_List.add(train);


//                                String[] string_ftl = find_train_List.toArray(new String[0]);


                                for (Train debug: find_train_List) {
                                    System.out.println("DEBUG *** FIND TRAIN LIST: " + debug);


//                                    for (int i = 0; i < find_train_List.size(); i++) {
//                                        string_ftl[i] = debug.toString();
//                                        System.out.println("string_ftl: " + string_ftl[i]);
//                                    }
                                }

                                //return;
                            }
                            else { Toast toast = Toast.makeText(getApplicationContext(),
                                        "Поезд не найден",
                                        Toast.LENGTH_LONG);
                                toast.show();
                               //return;
                            }
                        }
                        writeToCsvFile(find_train_List, newFile);
                        appendLog("найденные поезда" + find_train_List,logFile);


                        AlertDialog.Builder train_dialog = new AlertDialog.Builder(ActivityUsers.this);
                        train_dialog.setTitle("Список доступных поездов");

                        LayoutInflater inflater = LayoutInflater.from(ActivityUsers.this); //Получаем нужный шаблон
                        View lv = inflater.inflate(R.layout.lv,null);
                        train_dialog.setView(lv);

                        ListView mListView = lv.findViewById(R.id.listView);
                        //mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        TrainListAdapter adapter = new TrainListAdapter(ActivityUsers.this, R.layout.train_selection, find_train_List);
                        mListView.setAdapter(adapter);
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                                    long id) {
                                Train select_train = (Train) mListView.getItemAtPosition(position);
                                Log.d(TAG,"select_train" + select_train);
                                // Передача данных в FireBase

                                find_trains_tab.child("Train").setValue(select_train);

                                final Toast toast = Toast.makeText(getApplicationContext(),
                                        "Поезд оплачен",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        train_dialog.setNegativeButton("выйти", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                find_train_List.clear();
                            }
                        });
                        train_dialog.show();

                    }
                });

//                dataLines.add(string_ftl);
//                writeToCsvFile(dataLines,";",newFile);
//                dataLines2 = readFromCsvFile(";",newFile);
//                Log.d(TAG,"csv data: " + dataLines2.toString());
            }
        });
    }



    //Запись csv
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    public void writeToCsvFile(List<String[]> thingsToWrite, String separator,File newFile ){
//        try (FileWriter writer = new FileWriter(newFile)){
//            for (String[] strings : thingsToWrite) {
//                for (int i = 0; i < strings.length; i++) {
//                    writer.append(strings[i]);
//                    if(i < (strings.length-1))
//                        writer.append(separator);
//                }
//                writer.append(System.lineSeparator());
//            }
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showToastLong("Ошибка при записи в файл" + e.getMessage() );
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //чтение csv
//    public List<String[]> readFromCsvFile(String separator, File newFile){
//        try (BufferedReader reader = new BufferedReader(new FileReader(newFile))){
//            List<String[]> list = new ArrayList<>();
//            String line = "";
//            while((line = reader.readLine()) != null){
//                //csv_tv.setText(line);
//                String[] array = line.split(separator);
//                list.add(array);
//            }
//
//            return list;
//        } catch (IOException e) {
//            e.printStackTrace();
//           // showToastLong("Ошибка при чтении файла" + e.getMessage() );
//            return null;
//        }
//    }
    public void writeToCsvFile(ArrayList<Train> thingsToWrite,File newFile ){
    if (!newFile.exists())
    {
        try
        {
            newFile.createNewFile();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
        try
    {
        //BufferedWriter for performance, true to set append to file flag
        BufferedWriter buf = new BufferedWriter(new FileWriter(newFile, true));
        String text = thingsToWrite.toString();
        buf.append(text);
        buf.newLine();
        buf.close();
    }
        catch (IOException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();

    }
}

    public static void appendLog(String text, File logFile)
    {

        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    }

    private void showToastLong(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private void showToastShort(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG,"Menu item id" + id);
        switch (item.getItemId()) {
            case R.id.item1:
                Log.d(TAG,"Case item id" + id);
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                dialog1.setTitle("Помощь").setMessage("За помощью обращайтесь в техподдержку\n"
                        +"Телефон: 21-35-45");
                dialog1.show();
                break;
            case R.id.item2:
                Log.d(TAG,"Case item id" + id);
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                dialog2.setTitle("О программе").setMessage("Программу разработал студент БрГТУ \n"
                        +"Гончар Денис Владимирович");
                dialog2.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnDate1:
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog1 = new DatePickerDialog(ActivityUsers.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date1.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog1.show();
                break;
            case R.id.btnDate2:
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog2 = new DatePickerDialog(ActivityUsers.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date2.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog2.show();
                break;
        }

    }
}
