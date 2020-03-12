package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class InicioActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ViewHolder mViewHolder = new ViewHolder();
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private List<Aquarios> aquariosList;
    private List<Utilizador> utilizadorList;
    private String useremail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        aquariosList = new ArrayList<>();
        utilizadorList = new ArrayList<>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(InicioActivity.this, mDrawerLayout, R.string.abrir, R.string.sair);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mViewHolder.phvalue = findViewById(R.id.phvalue);
        mViewHolder.tempvalue = findViewById(R.id.tempvalue);
        mViewHolder.luzvalue = findViewById(R.id.luzvalue);
        mViewHolder.tpavalue = findViewById(R.id.tpavalue);
        mViewHolder.nav_nome = findViewById(R.id.nav_nome);
        mViewHolder.nav_email = findViewById(R.id.nav_email);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        useremail = user.getEmail();

        GetValuesUser();
        GetValuesAquario();

    }

    private void GetValuesAquario() {
        //Select * from Aquarios
        myRef = FirebaseDatabase.getInstance().getReference("Aquarios");
        //myRef.addListenerForSingleValueEvent();

        useremail = useremail.replace("@", "");
        useremail = useremail.replace(".", "");

        //SELECT * FROM WHERE email= currentUser
        Query query = FirebaseDatabase.getInstance().getReference("Aquarios")
                .orderByChild("email")
                .equalTo(useremail);
        query.addListenerForSingleValueEvent(valueEventListener);

    }

    private void GetValuesUser() {
        //Select * from Utilizadores
        myRef = FirebaseDatabase.getInstance().getReference("Utilizadores");

        Query query = FirebaseDatabase.getInstance().getReference("Utilizadores")
                .orderByChild("email")
                .equalTo(useremail);

        query.addListenerForSingleValueEvent(valueEventListenerUtilizador);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            aquariosList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Aquarios aquarios = snapshot.getValue(Aquarios.class);
                    aquariosList.add(aquarios);

                    mViewHolder.phvalue.setText(String.valueOf((Math.round(aquarios.getPh())*10) / 10));
                    mViewHolder.tempvalue.setText(String.valueOf((Math.round(aquarios.getTemp())*10)/10) + "ºC");
                    mViewHolder.luzvalue.setText(String.valueOf(3));
                    mViewHolder.tpavalue.setText(String.valueOf(10));
                }
            }
        }



        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
            if(id == R.id.home) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        else if (id == R.id.settings_light) {
            Intent i = new Intent(InicioActivity.this, definicoes_luzActivity.class);
            startActivityForResult(i, 1);
        } else if (id == R.id.sair) {
            setResult(0);
            Toast.makeText(this, "Sair", Toast.LENGTH_SHORT).show();
            finish();
        }
        return false;
    }





    private class ViewHolder {

        TextView phvalue;
        TextView tempvalue;
        TextView luzvalue;
        TextView tpavalue;
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
