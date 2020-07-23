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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
    private Integer lamp1=0;
    private Integer lamp2=0;
    private Integer lamp3=0;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes_luz);
        getWindow().setStatusBarColor(Color.BLACK);
        utilizadorList = new ArrayList<>();
        aquariosList = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewHolder.nav_nome = findViewById(R.id.nav_nome);
        mViewHolder.nav_email = findViewById(R.id.nav_email);
        mViewHolder.Brightness = findViewById(R.id.Brightness);
        mViewHolder.txt_intensidade = findViewById(R.id.txt_intensidade);
        mViewHolder.rbRed = findViewById(R.id.radio_red);
        mViewHolder.rbGreen = findViewById(R.id.radio_green);
        mViewHolder.rbBlue = findViewById(R.id.radio_blue);
        mViewHolder.rbWhite = findViewById(R.id.radio_white);
        mViewHolder.level1 = findViewById(R.id.level1);
        mViewHolder.level2 = findViewById(R.id.level2);
        mViewHolder.level3 = findViewById(R.id.level3);
        mViewHolder.lightAuto = findViewById(R.id.lightAuto);
        refreshLayout = findViewById(R.id.refreshLayout);


        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(definicoes_luzActivity.this, mDrawerLayout, R.string.abrir, R.string.sair);
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
        Refresh();

        mViewHolder.Brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                useremail = useremail.replace("@","");
                useremail = useremail.replace(".","");
                mFirebaseDatabase.getReference("Aquarios/"+useremail+"/brightness").setValue(progress);
                mViewHolder.txt_intensidade.setText(progress + " / 100");


                if(mViewHolder.lightAuto.isChecked()) {
                    mFirebaseDatabase.getReference("Aquarios/" + useremail + "/lightauto").setValue(0);
                    mViewHolder.lightAuto.setChecked(false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mViewHolder.level1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(lamp1 == 0) {
                        mViewHolder.level1.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                        mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        lamp1=1;
                        lamp2=0;
                        lamp3=0;
                    }
                    else if(lamp1==1 && lamp2 == 0){
                        mViewHolder.level1.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        lamp1 = 0;
                        lamp2 = 0;
                        lamp3 = 0;
                    }
                    else if(lamp1==1 && lamp2==1){
                        mViewHolder.level1.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                        mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        lamp1 = 1;
                        lamp2 = 0;
                        lamp3 = 0;
                    }
                useremail = useremail.replace("@","");
                useremail = useremail.replace(".","");
                if(lamp1 == 0 && lamp2==0 && lamp3 == 0)
                    mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampLevel").setValue(0);
                else
                mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampLevel").setValue(1);

                    if(mViewHolder.lightAuto.isChecked()) {
                        mFirebaseDatabase.getReference("Aquarios/" + useremail + "/lightauto").setValue(0);
                        mViewHolder.lightAuto.setChecked(false);
                    }
            }
        });

        mViewHolder.level2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(lamp2==0) {
                    mViewHolder.level1.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                    mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                    mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                    lamp1=1;
                    lamp2=1;
                    lamp3=0;
                }
                else if(lamp2==1 && lamp3==0){
                    mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                    mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                    lamp1=1;
                    lamp2=0;
                    lamp3=0;
                }
                else if(lamp2==1 && lamp3==1){
                    mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                    lamp1=1;
                    lamp2=1;
                    lamp3=0;
                }

                if(lamp1 == 1 && lamp2==0 && lamp3 == 0)
                    mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampLevel").setValue(1);
                else
                    mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampLevel").setValue(2);

                if(mViewHolder.lightAuto.isChecked()) {
                    mFirebaseDatabase.getReference("Aquarios/" + useremail + "/lightauto").setValue(0);
                    mViewHolder.lightAuto.setChecked(false);
                }
            }
        });

        mViewHolder.level3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(lamp3==0) {
                    mViewHolder.level1.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                    mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                    mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                    lamp1=1;
                    lamp2=1;
                    lamp3=1;
                }
                else if(lamp3==1){
                    mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                    lamp1=1;
                    lamp2=1;
                    lamp3=0;
                }

                if(lamp1 == 1 && lamp2==1 && lamp3 == 0)
                    mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampLevel").setValue(2);
                else
                    mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampLevel").setValue(3);

                if(mViewHolder.lightAuto.isChecked()) {
                    mFirebaseDatabase.getReference("Aquarios/" + useremail + "/lightauto").setValue(0);
                    mViewHolder.lightAuto.setChecked(false);
                }
            }
        });

        mViewHolder.lightAuto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mViewHolder.lightAuto.isChecked())
                    mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lightauto").setValue(1);
                else
                    mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lightauto").setValue(0);
            }
        });
    }

    private void Refresh(){

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetValuesUser();
                GetValuesAquario();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    public void notificationcall(){

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_wb_sunny_black_24dp)
                .setContentTitle("Luz Automática")
                .setContentText("A luz automática foi desativada");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());

    }

    public void RadioColorRed(View v){
        useremail = useremail.replace("@","");
        useremail = useremail.replace(".","");
        mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampColor").setValue(1);
    }

    public void RadioColorGreen(View v){
        useremail = useremail.replace("@","");
        useremail = useremail.replace(".","");
        mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampColor").setValue(2);
    }

    public void RadioColorBlue(View v){
        useremail = useremail.replace("@","");
        useremail = useremail.replace(".","");
        mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampColor").setValue(3);
    }

    public void RadioColorWhite(View v){
        useremail = useremail.replace("@","");
        useremail = useremail.replace(".","");
        mFirebaseDatabase.getReference("Aquarios/"+useremail+"/lampColor").setValue(4);
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

                    mViewHolder.Brightness.setProgress(aquarios.getBrightness());


                    int c = aquarios.getLampColor();

                    if(c == 1)
                        mViewHolder.rbRed.setChecked(true);
                    else if(c == 2)
                        mViewHolder.rbGreen.setChecked(true);
                    else if(c == 3)
                        mViewHolder.rbBlue.setChecked(true);
                    else if(c == 4)
                        mViewHolder.rbWhite.setChecked(true);


                    int l = aquarios.getLampLevel();

                    if(l == 1){
                        lamp1 = 1;
                        lamp2 = 0;
                        lamp3 = 0;
                        mViewHolder.level1.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                        mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                    }
                    else if(l == 2){
                        lamp1 = 1;
                        lamp2 = 1;
                        lamp3 = 0;
                        mViewHolder.level1.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                        mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                        mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                    }
                    else if(l == 3){
                        lamp1 = 1;
                        lamp2 = 1;
                        lamp3 = 1;
                        mViewHolder.level1.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                        mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                        mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_yellow_24dp);
                    }
                    else{
                        lamp1 = 0;
                        lamp2 = 0;
                        lamp3 = 0;
                        mViewHolder.level1.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        mViewHolder.level2.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                        mViewHolder.level3.setImageResource(R.drawable.ic_wb_incandescent_white_24dp);
                    }

                    int la = aquarios.getLightauto();

                    if(la == 0)
                        mViewHolder.lightAuto.setChecked(false);
                    else
                        mViewHolder.lightAuto.setChecked(true);

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
        } else if (id == R.id.settings_light) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.settings_tpa) {
            Intent i = new Intent(definicoes_luzActivity.this, definicoes_tpaActivity.class);
            startActivityForResult(i, 1);
        }
        else if(id == R.id.settings_temp){
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
        SeekBar Brightness;
        TextView txt_intensidade;
        RadioButton rbRed;
        RadioButton rbBlue;
        RadioButton rbWhite;
        RadioButton rbGreen;
        ImageView level1;
        ImageView level2;
        ImageView level3;
        CheckBox lightAuto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0)
            finish();
    }
}


