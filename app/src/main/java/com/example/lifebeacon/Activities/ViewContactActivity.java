package com.example.lifebeacon.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifebeacon.Adapters.DeleteContactAdapter;
import com.example.lifebeacon.Adapters.ViewContactAdapter;
import com.example.lifebeacon.Model.ContactData;
import com.example.lifebeacon.R;
import com.example.lifebeacon.databinding.ActivityViewContactBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewContactActivity extends AppCompatActivity {
        private ActivityViewContactBinding binding;
        private FirebaseFirestore db;
        private ViewContactAdapter adapter;
        private List<ContactData> viewContact;
        private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        viewContact = new ArrayList<>();

        recyclerView = binding.recyclarViewContact;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ViewContactAdapter(viewContact);
        recyclerView.setAdapter(adapter);

        db.collection("Trusted Contact").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ContactData contactData = document.toObject(ContactData.class);
                    viewContact.add(contactData);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "No Trusted Contact", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Something Wrong" + e, Toast.LENGTH_SHORT).show();

        });
    }
}