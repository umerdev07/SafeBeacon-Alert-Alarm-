package com.example.lifebeacon.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lifebeacon.R;
import com.example.lifebeacon.databinding.ActivityViewContactBinding;

public class ViewContactActivity extends AppCompatActivity {
        private ActivityViewContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(binding.getRoot());



    }
}