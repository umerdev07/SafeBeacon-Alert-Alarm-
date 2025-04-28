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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Double latitude, longitude;
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

        String finalSOS = SOS + " https://maps.google.com/?q=" + latitude + "," + longitude;
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());


        long expireAtMillis = System.currentTimeMillis() + (1 * 60 * 1000); // 1 minute in milliseconds
        Date expireAt = new Date(expireAtMillis);

        db.collection("Trusted Contact").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String phNumber = document.getString("phonenumber");

                    if (phNumber != null && !phNumber.isEmpty()) {
                        Map<String, Object> sosMessage = new HashMap<>();
                        sosMessage.put("contact", phNumber);
                        sosMessage.put("SOS Message", finalSOS);
                        sosMessage.put("latitude", latitude);
                        sosMessage.put("longitude", longitude);
                        sosMessage.put("TimeStamp", timestamp);
                        sosMessage.put("expireAt", expireAt);


                        db.collection("sosMessages").add(sosMessage)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "SOS Sent!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Check your Internet! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }else{
                        Toast.makeText(this, "Phone number is not fetch", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "No Trusted Contact", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to fetch Contact! Check your Internet", Toast.LENGTH_SHORT).show();
        });
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
