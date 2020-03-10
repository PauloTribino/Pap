package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String userID;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewHolder.et_email = findViewById(R.id.txt_email);
        mViewHolder.et_password = findViewById(R.id.txt_password);
        mViewHolder.bt_register = findViewById(R.id.btn_register);
        mViewHolder.bt_login = findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        //userID = user.getUid();

        mViewHolder.bt_login.setOnClickListener(new View.OnClickListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onClick(View view) {


                String email = mViewHolder.et_email.getText().toString();
                String password = mViewHolder.et_password.getText().toString();

                if (!email.equals("") && !password.equals("")) {

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();

                                if (flag) {
                                    flag = false;
                                    Toast.makeText(MainActivity.this, "Bem-Vindo!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(MainActivity.this, InicioActivity.class);


                                    i.putExtra("user", user.getEmail());
                                    i.putExtra("name", user);

                                    startActivity(i);
                                }

                            } else {
                                flag = true;
                                Toast.makeText(MainActivity.this, "Dados Inválidos!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    flag = true;

                    Toast.makeText(MainActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mViewHolder.bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegistoActivity.class);

                startActivity(i);
            }
        });

    }


    private void showData(DataSnapshot dataSnapshot) {
        ArrayList<String> array = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Utilizador utilizador = new Utilizador();
            utilizador.setEmail(ds.child(userID).getValue(Utilizador.class).getEmail());
            utilizador.setNome(ds.child(userID).getValue(Utilizador.class).getNome());
            utilizador.setPassword(ds.child(userID).getValue(Utilizador.class).getPassword());


            array.add(utilizador.getEmail());
            array.add(utilizador.getNome());
            array.add(utilizador.getPassword());
        }

    }

    public static class ViewHolder {

        EditText et_email;
        EditText et_password;
        Button bt_login;
        Button bt_register;

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        flag = true;
    }
}
