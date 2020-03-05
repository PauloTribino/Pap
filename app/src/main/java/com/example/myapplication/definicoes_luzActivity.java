package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class definicoes_luzActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private List<Aquarios> aquariosList;
    private List<Utilizador> utilizadorList;
    private String useremail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes_luz);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(definicoes_luzActivity.this, mDrawerLayout, R.string.abrir, R.string.sair);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        useremail = user.getEmail();

        GetValuesUser();
    }

    private void GetValuesUser() {
        //Select * from Utilizadores
        myRef = FirebaseDatabase.getInstance().getReference("Utilizadores");

        Query query = FirebaseDatabase.getInstance().getReference("Utilizadores")
                .orderByChild("email")
                .equalTo(useremail);

        query.addListenerForSingleValueEvent(valueEventListenerUtilizador);

    }


    ValueEventListener valueEventListenerUtilizador = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            utilizadorList.clear();
            mViewHolder.nav_nome = findViewById(R.id.nav_nome);
            mViewHolder.nav_email = findViewById(R.id.nav_email);

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Utilizador utilizador = snapshot.getValue(Utilizador.class);
                    utilizadorList.add(utilizador);
                    try {
                        mViewHolder.nav_nome.setText(String.valueOf(utilizador.getNome()));
                        mViewHolder.nav_email.setText(String.valueOf(utilizador.getEmail()));
                    } catch (Exception ec) {
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

        @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.home) {
            Intent i = new Intent(definicoes_luzActivity.this, InicioActivity.class);
            startActivityForResult(i, 1);
        }else if(id == R.id.settings) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.sair) {
            setResult(0);
            Toast.makeText(this, "Sair", Toast.LENGTH_SHORT).show();
            finish();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private class ViewHolder {


        TextView nav_nome;
        TextView nav_email;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0)
            finish();
    }
}


