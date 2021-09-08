package com.technominds.lecture.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technominds.lecture.socialmediaapp.CustomModels.UserModel;

public class HomeActivity extends AppCompatActivity {
    Toolbar home_toolbar;
    ImageView user_pic;

    TextView user_name, user_email;

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FirebaseUser muser;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        home_toolbar=findViewById(R.id.home_toolbar);
        setSupportActionBar(home_toolbar);

        drawerLayout=findViewById(R.id.home_drawer_layout);
        navigationView=findViewById(R.id.home_nav);

        //header references
        user_name=navigationView.getHeaderView(0).findViewById(R.id.head_name);
        user_email =navigationView.getHeaderView(0).findViewById(R.id.head_email);
        user_pic=navigationView.getHeaderView(0).findViewById(R.id.head_user_pic);


        muser=FirebaseAuth.getInstance().getCurrentUser();
        dbref= FirebaseDatabase.getInstance().getReference("users");



        dbref.child(muser.getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel m=snapshot.getValue(UserModel.class);
                user_name.setText(""+m.getName());
                user_email.setText(""+m.getEmail());
                Glide.with(HomeActivity.this).load(m.getImage_url())
                        .centerCrop()
                        .circleCrop()
                        .into(user_pic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        if(muser==null)
        {
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            finish();
        }

        toggle=new ActionBarDrawerToggle(this,drawerLayout,home_toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_profile:
                        startActivity(new Intent(HomeActivity.this,MyProfile.class));break;
                    case R.id.menu_logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        finish();
                        break;
                }
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

    }


}