package com.example.lifebeacon.Adapters;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DeleteContactAdapter extends RecyclerView.Adapter<DeleteContactAdapter.ViewHolder> {

    private List<ContactData> contactList;
    private FirebaseFirestore db;

    // Constructor with context to initialize db
    public DeleteContactAdapter(List<ContactData> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.del_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactData contact = contactList.get(position);
        holder.name.setText(contact.getName());
        holder.ph.setText(contact.getPhonenumber());

        db = FirebaseFirestore.getInstance();

        holder.del_icon.setOnClickListener(v -> {
            // Use getAdapterPosition() directly
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                ContactData contactToDelete = contactList.get(currentPosition);

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete this contact?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.collection("Trusted Contact").document(contactToDelete.getPhonenumber()).delete()
                                    .addOnSuccessListener(a -> {
                                        Toast.makeText(v.getContext(), "Contact Deleted", Toast.LENGTH_SHORT).show();
                                        contactList.remove(currentPosition);
                                        notifyItemRemoved(currentPosition);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(v.getContext(), "Failed to delete contact", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("No", null)
                        .setCancelable(true) // Allow dismissing the dialog by touching outside
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
