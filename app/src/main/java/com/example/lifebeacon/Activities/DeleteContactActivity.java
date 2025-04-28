package com.example.lifebeacon.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifebeacon.Adapters.DeleteContactAdapter;
import com.example.lifebeacon.Model.ContactData;
import com.example.lifebeacon.R;
import com.example.lifebeacon.databinding.ActivityDeleteContactBinding;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class DeleteContactActivity extends AppCompatActivity {

    private ActivityDeleteContactBinding binding;

    private FirebaseFirestore db;

    private DeleteContactAdapter adapter;
    private RecyclerView recyclerView;
    private List<ContactData>  delContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclarDeleteContact;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DeleteContactAdapter(delContact);
        recyclerView.setAdapter(adapter);

        db.collection("Trusted Contact").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                        ContactData contactData = document.toObject(ContactData.class);
                        delContact.add(contactData);

                }
                adapter.notifyDataSetChanged();
        })
                .addOnFailureListener(e -> {

                });
    }
}