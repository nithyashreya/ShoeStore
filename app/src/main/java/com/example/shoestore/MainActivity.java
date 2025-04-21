package com.example.shoestore;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoestore.Shoe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShoeAdapter shoeAdapter;
    private List<Shoe> shoeList;
    private DatabaseReference shoeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.shoeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoeList = new ArrayList<>();
        shoeAdapter = new ShoeAdapter(this, shoeList);
        recyclerView.setAdapter(shoeAdapter);

        shoeRef = FirebaseDatabase.getInstance().getReference("shoes");

        shoeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shoeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Shoe shoe = dataSnapshot.getValue(Shoe.class);
                    shoeList.add(shoe);
                }
                shoeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle error
            }
        });
    }
}
