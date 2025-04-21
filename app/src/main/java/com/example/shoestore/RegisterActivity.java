package com.example.shoestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmailEditText, registerPasswordEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmailEditText = findViewById(R.id.registerEmailEditText);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = registerEmailEditText.getText().toString();
        String password = registerPasswordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get the unique user ID
                        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                        // Create a reference to the Firebase Realtime Database
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                        // Create a new user object (you can add more fields as needed)
                        User newUser = new User(email, "Default Name", "Default Address");  // You can add more fields here.

                        // Save user data to Realtime Database
                        userRef.setValue(newUser).addOnCompleteListener(dbTask -> {
                            if (dbTask.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                finish();  // Go back to login page
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Registration failed
                        Toast.makeText(RegisterActivity.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
