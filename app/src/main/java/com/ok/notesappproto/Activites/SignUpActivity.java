package com.ok.notesappproto.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ok.notesappproto.databinding.ActivitySignUpBinding;

import org.jetbrains.annotations.NotNull;

import Models.Users;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    public Users user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = ActivitySignUpBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            getSupportActionBar().hide();

            auth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("signing you up please wait ...");


            if (auth.getCurrentUser() != null) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finishAffinity();
            }

            binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.userName.getText().toString().isEmpty()) {
                        binding.userName.setError("This cant be empty");
                        return;
                    }
                    if (binding.userEmail.getText().toString().isEmpty()) {
                        binding.userEmail.setError("This cant be empty");
                        return;
                    }
                    if (binding.userPass.getText().toString().isEmpty()) {
                        binding.userPass.setError("This cant be empty");
                        return;
                    }
                    progressDialog.show();
                    user = new Users();
                    user.setUserName(binding.userName.getText().toString());
                    user.setUserEmail(binding.userEmail.getText().toString());
                    user.setUserPass(binding.userPass.getText().toString());

                    auth.createUserWithEmailAndPassword(user.getUserEmail(), user.getUserPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user.setUserId(auth.getUid());
                                database.getReference().child("Users").child(user.getUserId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, "Account created successfully please sign in to your account", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

            binding.alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                }
            });
        }catch (Exception e){
            Log.d("Why", "onCreate: "+e.toString());
        }


    }
}