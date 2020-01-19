package com.app.kpp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.kpp.Models.BlackList;
import com.app.kpp.Models.Train;
import com.app.kpp.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class activity_admins extends AppCompatActivity {

    FirebaseDatabase dbU;
    DatabaseReference trains;
    DatabaseReference blackList_ref;

    private String TAG = "myLogs";

    private EditText et_admin_start_st;
    private EditText et_admin_end_st;
    private EditText start_date;
    private EditText end_date;
    private EditText start_time;
    private EditText end_time;
    private Button admin_btn_add;
    RelativeLayout RLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins);

        setTitle("Admins Activity");


        RLayout = findViewById(R.id.RelativeLayout);
        et_admin_start_st = findViewById(R.id.tv_admin_startst);
        et_admin_end_st = findViewById(R.id.tv_admin_endst);
        start_date = findViewById(R.id.et_admin_startDate);
        end_date = findViewById(R.id.et_admin_endDate);
        start_time = findViewById(R.id.et_admin_startTime);
        end_time = findViewById(R.id.et_admin_endTime);
        admin_btn_add = findViewById(R.id.admin_btn_add);
        dbU = FirebaseDatabase.getInstance();
        trains = dbU.getReference("Trains");
        blackList_ref = dbU.getReference("Users Blacklist");

        admin_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Train setTrain = new Train();
                setTrain.setStarting_sration(et_admin_start_st.getText().toString());
                setTrain.setEnd_station(et_admin_end_st.getText().toString());
                setTrain.setDate_departure(start_date.getText().toString());
                setTrain.setDate_arival(end_date.getText().toString());
                setTrain.setTime_start(start_time.getText().toString());
                setTrain.setTime_end(end_time.getText().toString());


                trains.push().setValue(setTrain)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Маршрут добавлен",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
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
            case R.id.item3:
                Log.d(TAG,"Case item id" + id);
                AlertDialog.Builder dialog3 = new AlertDialog.Builder(this);
                dialog3.setTitle("Чёрный список").setMessage("Введите данные пользователя");

                LayoutInflater inflater = LayoutInflater.from(this); //Получаем нужный шаблон
                View BlackList = inflater.inflate(R.layout.blacklistlayout, null);
                dialog3.setView(BlackList);

                //текстовые поля внутри шаблона
                final MaterialEditText email = BlackList.findViewById(R.id.emailField);
                final MaterialEditText reasonForBan = BlackList.findViewById(R.id.reasonForBan);

                dialog3.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog3.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BlackList user = new BlackList();
                        user.setEmail(email.getText().toString());
                        user.setReasonForBan(reasonForBan.getText().toString());
                        blackList_ref.push().setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Пользователь добавлен в чёрный список",
                                                Toast.LENGTH_SHORT);
                                        toast.show();

                                    }
                                });

                    }
                });
                dialog3.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
