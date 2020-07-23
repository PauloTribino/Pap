package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class definicoes_tpaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private List<Aquarios> aquariosList;
    private List<Utilizador> utilizadorList;
    private String useremail = "";
    private int tpa = 0;
    private int diastpa = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes_tpa);
        getWindow().setStatusBarColor(Color.BLACK);
        utilizadorList = new ArrayList<>();
        aquariosList = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       mViewHolder.starttpa = findViewById(R.id.starttpa);
       mViewHolder.dias = findViewById(R.id.dias);
       mViewHolder.btn_dias = findViewById(R.id.btn_dias);


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

       /* mViewHolder.btn_maxtemp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                float temp = Float.parseFloat(mViewHolder.maxtemp.getText().toString());
                Toast.makeText(definicoes_tpaActivity.this, "Valor definido", Toast.LENGTH_SHORT).show();
                mFirebaseDatabase.getReference("Aquarios/"+useremail+"/maxtemp").setValue(temp);
            }

        });*/

        mViewHolder.btn_dias.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                float temp = Float.parseFloat(mViewHolder.dias.getText().toString());
                Toast.makeText(definicoes_tpaActivity.this, "Data definido", Toast.LENGTH_SHORT).show();
                mFirebaseDatabase.getReference("Aquarios/"+useremail+"/dias").setValue(temp);
                diastpa = Integer.parseInt(mViewHolder.dias.getText().toString());
                CloseKeyboard();

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                int data = calendar.get(Calendar.DATE);

                data += diastpa;

                if(data > 30)
                    data = data - 30;

                mFirebaseDatabase.getReference("Aquarios/"+useremail+"/datafinaltpa").setValue(data);
            }

        });

    }


    public void starttpa(View v){

        if(tpa != 1){
            mFirebaseDatabase.getReference("Aquarios/"+useremail+"/tpa").setValue(1);

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            int data = calendar.get(Calendar.DATE);

            data += diastpa;

            if(data > 30)
                data = data - 30;

            mFirebaseDatabase.getReference("Aquarios/"+useremail+"/datafinaltpa").setValue(data);
        }
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

                   tpa = aquarios.getStarttpa();


                    diastpa = aquarios.getDias();
                   int diastemp = aquarios.getDias();
                   mViewHolder.dias.setText(Integer.toString(diastemp));

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

        } else if (id == R.id.settings_tpa) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.settings_temp){
            Intent i = new Intent(this, definicoes_tempActivity.class);
            startActivityForResult(i, 1);
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
        ImageView starttpa;
        TextView dias;
        Button btn_dias;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0)
            finish();
    }
}


