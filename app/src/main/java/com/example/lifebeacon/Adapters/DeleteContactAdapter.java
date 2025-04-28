package com.example.lifebeacon.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifebeacon.Model.ContactData;
import com.example.lifebeacon.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class DeleteContactAdapter extends RecyclerView.Adapter<DeleteContactAdapter.ViewHolder> {

    private List<ContactData> contactList;
    private FirebaseFirestore db;

    public DeleteContactAdapter(List<ContactData> contactList){
        this.contactList = contactList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.del_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ContactData contact = contactList.get(position);
        holder.name.setText(contact.getName());
        holder.ph.setText(contact.getPhonenumber());

        holder.del_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Contact")
                        .setMessage("Do you want to delete this contact!")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db = FirebaseFirestore.getInstance();

                            db.collection("Trusted Contact").whereEqualTo("phonenumber", contact.getPhonenumber())
                                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                DocumentReference docRef = document.getReference();
                                                docRef.delete().addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(v.getContext(), "Contact Deleted", Toast.LENGTH_SHORT).show();
                                                    contactList.remove(position);
                                                    notifyItemRemoved(position);
                                                }).addOnFailureListener(e -> {
                                                    Toast.makeText(v.getContext(), "Failed to delete contact: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, ph;
        ImageView del_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.del_contact_Name);
            ph = itemView.findViewById(R.id.del_contact_phone_number);
            del_icon = itemView.findViewById(R.id.delete_contacts);
        }
    }
}
