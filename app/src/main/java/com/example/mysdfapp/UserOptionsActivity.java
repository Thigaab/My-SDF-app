package com.example.mysdfapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserOptionsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);

        mAuth = FirebaseAuth.getInstance();

        Button changePasswordButton = findViewById(R.id.btnChangePassword);
        Button logoutButton = findViewById(R.id.btnLogout);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    mAuth.sendPasswordResetEmail(user.getEmail())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UserOptionsActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UserOptionsActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // User is not signed in, prompt them to sign in again
                    Toast.makeText(UserOptionsActivity.this, "User not signed in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserOptionsActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout button click
                mAuth.signOut();
                Intent intent = new Intent(UserOptionsActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // If user is not logged in, redirect to login activity
            Intent intent = new Intent(UserOptionsActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}
