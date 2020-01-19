package com.app.kpp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.kpp.Models.Admin;
import com.app.kpp.Models.BlackList;
import com.app.kpp.Models.Train;
import com.app.kpp.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth authUsers;  //для авторизации
    FirebaseAuth authAdmins;  //для авторизации
    FirebaseDatabase dbU;    //подключение к базе данных
    // FirebaseDatabase dbA;    //подключение к базе данных
    DatabaseReference users;    //работа с таблицами
    DatabaseReference admins;//работа с таблицами

    List<String> AdminTable;
    private static final String TAG = "myLogs";
    RelativeLayout root;

    // Получаем экземпляр класса, который будем использовать для записи логов.
    // Передаваемый параметр LogApp - имя логера.
    // В документации сказано, что имя логгера должно совпадать с именем
    // класса или пакета ().
    Logger logger = Logger.getLogger(MainActivity.class.getName());

    File logFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        root = findViewById(R.id.root_element);

        authUsers = FirebaseAuth.getInstance();
        authAdmins = FirebaseAuth.getInstance();
        dbU = FirebaseDatabase.getInstance();
        //dbA = FirebaseDatabase.getInstance();
        users = dbU.getReference("Users");
        admins = dbU.getReference("Admins");

        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        logFile = new File(path + "/Log.txt");

        clearLog(logFile);

        // Создаём handler, который будет записывать лог
        // в файл "LogApp". Символ "%t" указывает на то, что файл
        // будет располагаться в папке с системными временными файлами.
        try {
            FileHandler fh = new FileHandler("%tLogApp");
            logger.addHandler(fh);
        } catch (SecurityException e) {
            logger.log(Level.SEVERE, "Не удалось создать файл лога из-за политики безопасности.", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Не удалось создать файл лога из-за ошибки ввода-вывода.", e);
        }
        logger.log(Level.INFO, "Запись лога с уровнем INFO (информационная)");
        logger.log(Level.WARNING,"Запись лога с уровнем WARNING (Предупреждение)");
        logger.log(Level.SEVERE, "Запись лога с уровнем SEVERE (серъёзная ошибка)");


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInWindow();
            }
        });

        FireBaseDatabaseHelper helper = new FireBaseDatabaseHelper();
        helper.readAdmins(new FireBaseDatabaseHelper.DataAdminStatus() {
            @Override
            public void dataAdminStatus(List<Admin> adminList, List<String> keys) {
                for (int i = 0; i < adminList.size(); i++) {
                    System.out.println(adminList.get(i));
                    Log.d(TAG, "dataAdminStatus" + adminList.get(i));
                    ;
                }
            }

        });


        helper.readTrains(new FireBaseDatabaseHelper.DataTrainStatus() {
            @Override
            public void dataTrainStatus(List<Train> trainsList, List<String> keys) {
                for (int i = 0; i < trainsList.size(); i++) {
                    System.out.println(trainsList.get(i));
                    Log.d(TAG, "DataTrainsLoaded" + trainsList.get(i));
                }
            }
        });

//        helper.readBlacklist(new FireBaseDatabaseHelper.DataBlacklistStatus() {
//            @Override
//            public void dataBlacklistStatus(List<BlackList> Blacklist, List<String> keys) {
//                for (int i = 0; i < Blacklist.size(); i++) {
//                    System.out.println(Blacklist.get(i));
//                    Log.d(TAG, "DataTrainsLoaded" + Blacklist.get(i));
//                }
//            }
//        });
    }

    private void showRegisterWindow() {

        logger.log(Level.WARNING,"Запись лога с уровнем WARNING (Предупреждение)\n"+
                "Метод showRegisterWindow");
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("зарегистрироваться").setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this); //Получаем нужный шаблон
        View register_window = inflater.inflate(R.layout.register_window, null);
        dialog.setView(register_window);

        //текстовые поля внутри шаблона
        final MaterialEditText email = register_window.findViewById(R.id.emailField);
        final MaterialEditText pass = register_window.findViewById(R.id.passField);
        final MaterialEditText name = register_window.findViewById(R.id.nameField);
        final MaterialEditText phone = register_window.findViewById(R.id.phoneField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("oncklick");
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль который длинее 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // регистрация пользователя
                authUsers.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setPhone(phone.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(root, "Пользователь добавлен", Snackbar.LENGTH_SHORT).show();

                                            }
                                        });

                            }
                        });
                //Регистрация админа
              /* authAdmins.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Admin admin = new Admin();
                                admin.setEmail(email.getText().toString());
                                admin.setPass(pass.getText().toString());
                                admin.setName(name.getText().toString());
                                admin.setPhone(phone.getText().toString());


                                admins.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(admin)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(root,"Админ добавлен", Snackbar.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(root,"Админ не быол добавлен"+ e.getMessage(), Snackbar.LENGTH_LONG).show();

                                    }
                                });

                            }
                        });
*/
            }
        });
        dialog.show();

    }

    private void showSignInWindow() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти").setMessage("Введите данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this); //Получаем нужный шаблон
        View SignInWindow = inflater.inflate(R.layout.activity_sign_in, null);
        dialog.setView(SignInWindow);

        //текстовые поля внутри шаблона
        final MaterialEditText email = SignInWindow.findViewById(R.id.emailField);
        final MaterialEditText pass = SignInWindow.findViewById(R.id.passField);

        setNegativeButton(dialog);
        setPositiveButton(dialog, email, pass);
        dialog.show();

    }

    private void setNegativeButton(AlertDialog.Builder dialog) {
        dialog.setNegativeButton("Отменить", (DialogInterface dialogInterface, int i) -> dialogInterface.dismiss());
    }


    private void setPositiveButton(AlertDialog.Builder dialog, MaterialEditText email, MaterialEditText pass) {
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            // startActivity(new Intent(MainActivity.this, ActivityUsers.class));//TODO
                if (!validatePositiveBtnOnClick(email, pass)) return;
                FireBaseDatabaseHelper helperAdmin = new FireBaseDatabaseHelper();
                helperAdmin.readAdmins(this::getDataAdminStatus);
                FireBaseDatabaseHelper helperBlack = new FireBaseDatabaseHelper();
                helperBlack.readBlacklist(this::getDataBlackListStatus);

            }

            private boolean validatePositiveBtnOnClick(MaterialEditText email, MaterialEditText pass) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    ActivityUsers.appendLog("Log.Error: Пользователь не ввёл почту",logFile);
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if (pass.getText().toString().length() < 5) {
                    ActivityUsers.appendLog("Log.Error: Пользователь ввёл пароль короче 5 символов",logFile);
                    Snackbar.make(root, "Введите пароль короче 5 символов", Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }

            private void getDataBlackListStatus(List<BlackList> blacklist, List<String>keys) {
                       // for (int i = 0; i < adminList.size(); i++)
                        for (BlackList BlackUser : blacklist)
                        {
                            if (email.getText().toString().equals(BlackUser.getEmail()))
                            {
                                final Toast toast = Toast.makeText(getApplicationContext(),
                                        "Пользователь в чёрном списке",
                                        Toast.LENGTH_LONG);
                                toast.show();
//                                Thread t = new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                    }
//                                });
                                //return;
                               // CompletableFuture.runAsync(() -> System.out.println("Мы в новом потоке"));
                            }
                        }
            }

            private void getDataAdminStatus(List<Admin> adminList, List<String>keys) {
                   // for (int i = 0; i < adminList.size(); i++)
                    for (Admin admin : adminList) {
                        Log.d(TAG, "Auth_Admin:" + admin.getEmail());
                        System.out.println(admin.getEmail());
                     /* Map<List<Admin>, ClipData.Item> map = new HashMap<>();
                       for(ClipData.Item item : adminList) {
                           map.put(Arrays.asList(item.name, item.type), item);
                       }
                       Collection<ClipData.Item> unique = map.values();*/

                        if (email.getText().toString().equals(admin.getEmail())) {
                            //if (email.getText().toString().equals(admin.getEmail())){
                            authAdmins.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                                    .addOnSuccessListener(authResult ->
                                            startActivity(new Intent(MainActivity.this, activity_admins.class))
                                    ).addOnFailureListener(e ->
                                    Snackbar.make(root, "Ошибка авторизации" + e.getMessage(), Snackbar.LENGTH_SHORT).show()
                            );
                             return;
                        }
                    }

                            authUsers.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            startActivity(new Intent(MainActivity.this, ActivityUsers.class));
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(root, "Ошибка авторизации" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                                }
                            });
                      return;
            }
        });
    }

    public void clearLog(File logFile) {

        if (logFile.exists()) {
            try {
                logFile.delete();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
