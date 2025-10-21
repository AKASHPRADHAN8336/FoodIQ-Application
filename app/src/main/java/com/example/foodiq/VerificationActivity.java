package com.example.foodiq;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {


    TextView mobileNumber,resendOTP;
    EditText num1,num2,num3,num4,num5,num6;
    Button verifyBtn;
    ProgressBar progressBar;

    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mobileNumber = findViewById(R.id.textView13);
        resendOTP = findViewById(R.id.textView61);
        num1 = findViewById(R.id.editTextNumber);
        num2 = findViewById(R.id.editTextNumber2);
        num3 = findViewById(R.id.editTextNumber3);
        num4 = findViewById(R.id.editTextNumber4);
        num5 = findViewById(R.id.editTextNumber5);
        num6 = findViewById(R.id.editTextNumber6);
        progressBar = findViewById(R.id.progressBar);
        verifyBtn = findViewById(R.id.button);

        String number = getIntent().getStringExtra("mobile");
        mobileNumber.setText(number);

        verificationId = getIntent().getStringExtra("verificationId");

        sendOtpInputs();



        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num1.getText().toString().trim().isEmpty() ||
                        num2.getText().toString().trim().isEmpty() ||
                        num3.getText().toString().trim().isEmpty() ||
                        num4.getText().toString().trim().isEmpty() ||
                        num5.getText().toString().trim().isEmpty() ||
                        num6.getText().toString().trim().isEmpty() ){
                    Toast.makeText(VerificationActivity.this,"Please enter valid code",Toast.LENGTH_LONG).show();
                    return;
                }

                String code = num1.getText().toString() +
                        num2.getText().toString() +
                        num3.getText().toString() +
                        num4.getText().toString() +
                        num5.getText().toString() +
                        num6.getText().toString() ;

                if(verificationId!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    verifyBtn.setVisibility(View.INVISIBLE);

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            verifyBtn.setVisibility(View.VISIBLE);

                            if(task.isSuccessful()){
                                Intent intent = new Intent(getApplicationContext(),profileActivity.class);
                                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(VerificationActivity.this,"Verifcation code is invalid",Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(getIntent().getStringExtra("mobile"),
                        60, TimeUnit.SECONDS, VerificationActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(VerificationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

    }

    private void sendOtpInputs(){
        EditText[] editText = {num1,num2,num3,num4,num5,num6};
        for (int i=0;i<editText.length-1;i++){
            final int index = i;
            editText[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(!s.toString().trim().isEmpty()){
                        editText[index+1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}