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
        binding = ActivityEditSosactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editSOS = binding.editSOS.getText().toString();
                Intent intent = new Intent(EditSOSActivity.this, MainActivity.class);

                if (!editSOS.isEmpty()) {
                    intent.putExtra("SOS_Message", editSOS);
                }

                startActivity(intent);
            }
        });
    }
}
