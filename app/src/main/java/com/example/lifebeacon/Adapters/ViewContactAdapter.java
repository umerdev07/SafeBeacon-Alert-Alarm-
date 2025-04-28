package com.example.lifebeacon.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifebeacon.Model.ContactData;
import com.example.lifebeacon.R;

import org.w3c.dom.ls.LSException;

import java.util.List;

public class ViewContactAdapter extends RecyclerView.Adapter<ViewContactAdapter.ViewHolder> {

    private List<ContactData> viewContact;

    public ViewContactAdapter(List<ContactData>viewContact){
        this.viewContact = viewContact;
    }

    @NonNull
    @Override
    public ViewContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewContactAdapter.ViewHolder holder, int position) {
        ContactData contact = viewContact.get(position);
        holder.name.setText(contact.getName());
        holder.ph.setText(contact.getPhonenumber());
    }

    @Override
    public int getItemCount() {
        return viewContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ph, name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.view_contact_Name);
            ph = itemView.findViewById(R.id.view_contact_phone_number);
        }
    }
}
