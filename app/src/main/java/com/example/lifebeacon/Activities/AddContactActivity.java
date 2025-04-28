package com.example.lifebeacon.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lifebeacon.R;
import com.example.lifebeacon.databinding.ActivityAddContactBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {
    private ActivityAddContactBinding binding;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        binding.saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addName = binding.contactNameEditText.getText().toString();
                String addPhoneNumber = binding.contactNumberEditText.getText().toString();

                if (!addName.isEmpty() && !addPhoneNumber.isEmpty()) {
                    Map<String, Object> contact = new HashMap<>();
                    contact.put("name", addName);
                    contact.put("phonenumber", addPhoneNumber);

                    db.collection("Trusted Contact")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                int count = queryDocumentSnapshots.size() + 1;
                                String docName = "Contact " + count;

                                db.collection("Trusted Contact")
                                        .document(docName)
                                        .set(contact)
                                        .addOnSuccessListener(aVoid -> {
                                            binding.contactNumberEditText.setText("");
                                            binding.contactNameEditText.setText("");
                                            Toast.makeText(AddContactActivity.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(AddContactActivity.this, "Failed to save contact", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(AddContactActivity.this, "Failed to count contacts", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(AddContactActivity.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}