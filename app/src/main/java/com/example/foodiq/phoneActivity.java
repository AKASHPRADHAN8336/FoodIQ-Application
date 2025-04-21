package com.example.foodiq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class phoneActivity extends AppCompatActivity {

    CountryCodePicker countryCode;
    EditText mobileNumber;
    Button sendOtpbtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phone);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        countryCode = findViewById(R.id.countryCodeHolder);
        mobileNumber = findViewById(R.id.editText);
        sendOtpbtn = findViewById(R.id.otp_btn);
        progressBar = findViewById(R.id.progressBar);



        sendOtpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobileNumber.getText().toString().trim().isEmpty()){
                    Toast.makeText(phoneActivity.this,"Please enter mobile number",Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                sendOtpbtn.setVisibility(View.INVISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(countryCode.getSelectedCountryCodeWithPlus() + mobileNumber.getText().toString(),
                        60, TimeUnit.SECONDS, phoneActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                sendOtpbtn.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                sendOtpbtn.setVisibility(View.VISIBLE);
                                Toast.makeText(phoneActivity.this,"hello",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verficationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        super.onCodeSent(s, forceResendingToken);
                                progressBar.setVisibility(View.GONE);
                                sendOtpbtn.setVisibility(View.VISIBLE);

                                Intent intent = new Intent(phoneActivity.this,VerificationActivity.class);
                                intent.putExtra("mobile",mobileNumber.getText().toString());
                                intent.putExtra("verficationId",verficationId);
                                startActivity(intent);
                            }
                        });
            }
        });
    }
}