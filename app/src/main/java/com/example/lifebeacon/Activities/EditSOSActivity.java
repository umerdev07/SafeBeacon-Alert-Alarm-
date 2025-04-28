package com.example.lifebeacon.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lifebeacon.MainActivity;
import com.example.lifebeacon.R;
import com.example.lifebeacon.databinding.ActivityEditSosactivityBinding;

public class EditSOSActivity extends AppCompatActivity {

    private ActivityEditSosactivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.saveSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SOS = "Please Help! I am in trouble";
                String editSOS = binding.editSOS.getText().toString();
                if(!editSOS.isEmpty()){
                    Intent intent = new Intent(EditSOSActivity.this, MainActivity.class);
                    intent.putExtra("SOS_Message", editSOS);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(EditSOSActivity.this, MainActivity.class);
                    intent.putExtra("default_Message", SOS);
                    startActivity(intent);
                }
            }
        });
    }
}