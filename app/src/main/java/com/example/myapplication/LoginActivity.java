package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.object.MessageEvent;
import com.example.myapplication.object.User;
import com.example.myapplication.utils.CommontUtils;
import com.example.myapplication.utils.Constant;
import com.example.myapplication.utils.DatabaseUtil;
import com.example.myapplication.utils.StringUtil;
import com.example.myapplication.utils.TinyDB;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    List<User> userList;
    EditText edUserName;
    EditText edUserPass;
    DatabaseUtil databaseUtil;
    private DatabaseReference mDatabase;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("key1").setValue("JunYoung");

        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.btnQuit.setOnClickListener(this);

        edUserName = findViewById(R.id.edUserName);
        edUserPass = findViewById(R.id.edUserPass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            databaseUtil = new DatabaseUtil(this);
            userList = databaseUtil.getAllUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:

                boolean isLoginSuccess = false;
                if (StringUtil.isNullOrEmpty(edUserName.getText().toString()))
                    {
                    edUserName.setError("Input username");
                }
                else if (StringUtil.isNullOrEmpty(edUserPass.getText().toString()))
                    {
                    edUserPass.setError("Input password");
                }
                else
                    {
                    if (userList != null && userList.size() > 0) {
                        for (User user : userList) {
                            if (user.getUsername().equals(edUserName.getText().toString())
                                    && user.getPassword().equals(edUserPass.getText().toString())) {
                                isLoginSuccess = true;

                                TinyDB tinydb = new TinyDB(this);
                                tinydb.putObject("current_user", user);

                                CommontUtils.hideKeyboard(this);
                                Toast.makeText(this, "Login successfully !", Toast.LENGTH_SHORT).show();
                                Intent iMain = new Intent(this, MainActivity.class);
                                startActivity(iMain);
                                finish();
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(this, "Cannot access database!", Toast.LENGTH_SHORT).show();
                    }

                    if (!isLoginSuccess) {
                        Toast.makeText(this, "Username or password is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnRegister:
                Intent iRegister = new Intent(this, RegisterActivity.class);
                startActivity(iRegister);
                break;
            case R.id.btnQuit:
                finish();
                break;
            default:
                break;
        }
    }



}