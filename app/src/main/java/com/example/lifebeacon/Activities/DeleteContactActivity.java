package com.example.lifebeacon.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifebeacon.Adapters.DeleteContactAdapter;
import com.example.lifebeacon.Model.ContactData;
import com.example.lifebeacon.R;
import com.example.lifebeacon.databinding.ActivityDeleteContactBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeleteContactActivity extends AppCompatActivity {

    private ActivityDeleteContactBinding binding;
    private FirebaseFirestore db;
    private DeleteContactAdapter adapter;
    private RecyclerView recyclerView;
    private List<ContactData> delContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        delContact = new ArrayList<>();

        recyclerView = binding.recyclarDeleteContact;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DeleteContactAdapter(delContact);
        recyclerView.setAdapter(adapter);

        db.collection("Trusted Contact").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ContactData contactData = document.toObject(ContactData.class);
                            delContact.add(contactData);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("DeleteContactActivity", "No contacts found.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteContactActivity", "Error getting documents", e);
                });
    }
}
