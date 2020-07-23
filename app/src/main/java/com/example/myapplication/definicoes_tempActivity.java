package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class definicoes_tempActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_definicoes_temp);
        getWindow().setStatusBarColor(Color.BLACK);
        utilizadorList = new ArrayList<>();
        aquariosList = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewHolder.maxtemp = findViewById(R.id.maxtemp);
        mViewHolder.btn_maxtemp = findViewById(R.id.btn_maxtmep);


        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.abrir, R.string.sair);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        useremail = user.getEmail();

        GetValuesUser();
        GetValuesAquario();

        mViewHolder.btn_maxtemp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                float temp = Float.parseFloat(mViewHolder.maxtemp.getText().toString());
                Toast.makeText(definicoes_tempActivity.this, "Valor definido", Toast.LENGTH_SHORT).show();
                mFirebaseDatabase.getReference("Aquarios/"+useremail+"/maxtemp").setValue(temp);
                CloseKeyboard();
            }

        });

    }

    private void CloseKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
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
            mViewHolder.nav_nome = findViewById(R.id.nav_nome);
            mViewHolder.nav_email = findViewById(R.id.nav_email);
            utilizadorList.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Utilizador utilizador = snapshot.getValue(Utilizador.class);
                    utilizadorList.add(utilizador);
                    /*try {*/
                    mViewHolder.nav_nome.setText(String.valueOf(utilizador.getNome()));
                    mViewHolder.nav_email.setText(String.valueOf(utilizador.getEmail()));
                    /*} catch (Exception ec) {
                    }*/

                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

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

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            aquariosList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Aquarios aquarios = snapshot.getValue(Aquarios.class);
                    aquariosList.add(aquarios);

                    float temp = aquarios.getMaxtemp();

                    mViewHolder.maxtemp.setText(Float.toString(temp));

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
            Intent i = new Intent(this, InicioActivity.class);
            startActivityForResult(i, 1);
        } else if (id == R.id.settings_light) {
            Intent i = new Intent(this, definicoes_luzActivity.class);
            startActivityForResult(i, 1);

        }
        else if (id == R.id.settings_tpa) {
            Intent i = new Intent(this, definicoes_tpaActivity.class);
            startActivityForResult(i, 1);
        }
        else if (id == R.id.settings_temp) {
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
        TextView maxtemp;
        Button btn_maxtemp;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0)
            finish();
    }
}


