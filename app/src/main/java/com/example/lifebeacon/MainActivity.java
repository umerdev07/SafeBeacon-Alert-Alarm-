package com.example.lifebeacon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
    private Double longitude, latitude;
    private FirebaseFirestore db;

    private static final String ACCOUNT_SID = "AC33a6c86e043e03394b6b8c2900907e74";
    private static final String AUTH_TOKEN = "c9d498c6d5b8613821a2fc884be52fbb";
    private static final String TWILLO_PHONE_NUMBER = "+18382218709";

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

        getCurrentLocation();

        binding.alertBtn.setOnClickListener(v -> {
            if (latitude != null && longitude != null) {
                sendSOSMessage();
            } else {
                Toast.makeText(MainActivity.this, "Location not available yet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendSOSMessage() {
        Intent intent = getIntent();
        String SOS = intent.getStringExtra("SOS_Message");

        if (SOS == null || SOS.isEmpty()) {
            SOS = "Please Help! I am in trouble";
        }

        String SOSMessage = SOS + " https://maps.google.com/?q=" + latitude + "," + longitude;
       
        db.collection("Trusted Contact").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (var document : queryDocumentSnapshots.getDocuments()) {
                            String contact = document.getString("number");
                            if (contact != null && !contact.isEmpty()) {
                                Map<String, Object> sosMessage = new HashMap<>();
                                sosMessage.put("contact", contact);
                                sosMessage.put("message", SOSMessage);
                                sosMessage.put("latitude", latitude);
                                sosMessage.put("longitude", longitude);
                                sosMessage.put("timestamp", System.currentTimeMillis());

                                db.collection("sos_messages")
                                        .add(sosMessage)
                                        .addOnSuccessListener(docRef -> {
                                            Log.d("Firestore", "SOS message successfully added to sos_messages collection!");
                                            Toast.makeText(MainActivity.this, "SOS Sent!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("FirestoreError", "Error adding SOS message: " + e.getMessage());
                                            Toast.makeText(MainActivity.this, "Failed to send SOS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No Trusted Contacts Found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Failed to fetch contacts: " + e.getMessage());
                    Toast.makeText(MainActivity.this, "Failed to fetch contacts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        } else {
                            Toast.makeText(this, "Location is not available", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error fetching location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
