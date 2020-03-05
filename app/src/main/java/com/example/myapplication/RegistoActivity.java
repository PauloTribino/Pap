package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegistoActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        mViewHolder.et_email = findViewById(R.id.txt_email);
        mViewHolder.et_nome =  findViewById(R.id.txt_nome);
        mViewHolder.et_password =  findViewById(R.id.txt_password);
        mViewHolder.bt_register =  findViewById(R.id.btn_register);
        mViewHolder.txt_backlogin =  findViewById(R.id.txt_backlogin);

        mAuth = FirebaseAuth.getInstance();

        mViewHolder.bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mViewHolder.et_email.getText().toString();
                final String nome = mViewHolder.et_nome.getText().toString();
                final String password = mViewHolder.et_password.getText().toString();

                if (!email.equals("") && !nome.equals("") && !password.equals("")) {


                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Utilizador utilizador = new Utilizador(email,nome,password);

                                FirebaseDatabase.getInstance().getReference("Utilizadores").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(utilizador).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegistoActivity.this,"Registado com sucesso",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else{
                                            Toast.makeText(RegistoActivity.this, "Email já utilizado!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    Toast.makeText(RegistoActivity.this,"Palavra-Passe demasiado curta!", Toast.LENGTH_LONG).show();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(RegistoActivity.this, "Caracteres Inválidos!", Toast.LENGTH_SHORT).show();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(RegistoActivity.this, "Email já utilizado!", Toast.LENGTH_SHORT).show();
                                } catch(Exception e) {
                                    Log.e("TAG", e.getMessage());
                                }

                            }
                        }
                    });

                } else {
                    Toast.makeText(RegistoActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mViewHolder.txt_backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {

        }
    }

    public static class ViewHolder {
        EditText et_email;
        EditText et_nome;
        EditText et_password;
        Button bt_register;
        TextView txt_backlogin;
    }

}
