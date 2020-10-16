package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.databinding.ActivityRegisterBinding;
import com.example.myapplication.object.MessageEvent;
import com.example.myapplication.object.User;
import com.example.myapplication.utils.Constant;
import com.example.myapplication.utils.DatabaseUtil;
import com.example.myapplication.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    RadioGroup radioSex;
    RadioButton btnMale;
    RadioButton btnFeMale;
    EditText edUserName;
    EditText edPass;
    EditText edWeight;
    DatabaseUtil databaseUtil;

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnRegister.setOnClickListener(this);
        binding.btnQuit.setOnClickListener(this);

        radioSex = findViewById(R.id.radioSex);
        btnMale = findViewById(R.id.radioMale);
        btnFeMale = findViewById(R.id.radioFemale);
        btnMale.setChecked(true);

        edUserName = findViewById(R.id.edUserName);
        edPass = findViewById(R.id.edUserPass);
        edWeight = findViewById(R.id.edWeight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                int selected = radioSex.getCheckedRadioButtonId();
                int gender = -1;
                if (selected == btnFeMale.getId()) {
                    gender = 0;
                } else {
                    gender = 1;
                }
                if (StringUtil.isNullOrEmpty(edUserName.getText().toString())){
                    edUserName.setError("Input username");
                } else if (StringUtil.isNullOrEmpty(edPass.getText().toString())){
                    edPass.setError("Input password");
                } else if(StringUtil.isNullOrEmpty(edWeight.getText().toString())){
                    edWeight.setError("Input weight");
                } else {
                    User user = new User();
                    user.setUsername(edUserName.getText().toString());
                    user.setPassword(edPass.getText().toString());
                    user.setWeight(edWeight.getText().toString());
                    user.setGender(String.valueOf(gender));

                    databaseUtil = new DatabaseUtil(this);
                    databaseUtil.saveUserInDB(user);
                }
                break;
            case R.id.btnQuit:
                finish();
                break;
            default:
                break;
        }
        
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getKey() == "SAVE_USER_TO_DB_SUCCESS" && (Boolean) event.getObject() == true){
            Toast.makeText(this, "Successfully Create user acount !", Toast.LENGTH_SHORT).show();
            finish();
        }
    };
}