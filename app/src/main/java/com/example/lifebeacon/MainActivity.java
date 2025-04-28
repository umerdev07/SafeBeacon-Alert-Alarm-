package com.example.lifebeacon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lifebeacon.Activities.MenuActivity;
import com.example.lifebeacon.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Double latitude = null, longitude = null;
    private FirebaseFirestore db;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        binding.menu.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        binding.alertBtn.setOnClickListener(v -> {
            if (latitude != null && longitude != null) {
                sendSOSMessage();
            } else {
                Toast.makeText(MainActivity.this, "Fetching location, please wait...", Toast.LENGTH_SHORT).show();
                getCurrentLocation(); // Try fetching again
            }
        });

        getCurrentLocation(); // Start fetching location
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d(TAG, "Location obtained: " + latitude + ", " + longitude);
                        } else {
                            Toast.makeText(this, "Location is not available", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Location is null");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error fetching location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Location error: " + e.getMessage());
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void sendSOSMessage() {
        Intent intent = getIntent();
        String SOS = intent.getStringExtra("SOS_Message");

        if (SOS == null || SOS.trim().isEmpty()) {
            SOS = "Please Help! I am in trouble.";
        }

        String finalSOS = SOS;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Location permission denied");
            }
        }
    }
}
