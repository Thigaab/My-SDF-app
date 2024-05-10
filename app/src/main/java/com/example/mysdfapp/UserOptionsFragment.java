package com.example.mysdfapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserOptionsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private MainActivity _mainActivity;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.i("MyPostListFragment","Fragment's on attach");
        if (context instanceof MainActivity){
            _mainActivity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_options, container, false);

        mAuth = FirebaseAuth.getInstance();

        Button changePasswordButton = view.findViewById(R.id.btnChangePassword);
        Button logoutButton = view.findViewById(R.id.btnLogout);

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
                                        Toast.makeText(_mainActivity, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(_mainActivity, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // User is not signed in, prompt them to sign in again
                    Toast.makeText(_mainActivity, "User not signed in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(_mainActivity, Login.class);
                    startActivity(intent);
                    _mainActivity.finish();
                }
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout button click
                mAuth.signOut();
                Intent intent = new Intent(_mainActivity, Login.class);
                startActivity(intent);
                _mainActivity.finish();
            }
        });

        _mainActivity.setToolbarTitle("User settings");
        _mainActivity.setBackButtonEnabled(true);
        _mainActivity.showFloatingButton(false);

        return view;
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // If user is not logged in, redirect to login activity
            Intent intent = new Intent(_mainActivity, Login.class);
            startActivity(intent);
            _mainActivity.finish();
        }
    }*/
}
