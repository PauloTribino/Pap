package com.example.myapplication;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class DBHelper {

    private DatabaseReference mDatabase;


    public DBHelper(DatabaseReference mDatabase){
        this.mDatabase=mDatabase;
    }

    public long RegistarUtilizador(String email, String nome, String password) {

        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("Utilizador").push().getKey();
        Utilizador utilizador = new Utilizador(email,nome,password);

        Map<String, Object> childUpdates = new HashMap<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        boolean res = mDatabase.child("email").orderByChild("Utilizador").equals(utilizador.getEmail());

       if(res!=true){
           childUpdates.put("/Utilizador/" + key, utilizador);
           mDatabase.updateChildren(childUpdates);
           return 1;
       }
       else{
            return 0;
       }
    }

}
