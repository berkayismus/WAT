package com.berkaycayli.wat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRegister extends AppCompatActivity {

    private FirebaseAuth userAuth;
    private EditText emailText,passwordText;
    String email,password;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userColRef = db.collection("Users");

    @Override
    protected void onStart() {
        super.onStart();
        // Daha önceden giriş yapmış kullanıcı varsa, onu yönlendirelim
        // Yönlendirme fonksiyonu -> updateUI()
        FirebaseUser currentUser = userAuth.getCurrentUser();
        updateUI(currentUser);


    }


    public void updateUI(FirebaseUser currentUser){
        // giriş yapmış kullanıcı varsa, yani currentUser boş değilse aşağıdakini yap
        if(currentUser!=null){
            Intent intent = new Intent(getApplicationContext(),GuestActivity.class);
            startActivity(intent);
            finish();
        } else{
            // giriş yapmış kullanıcı yoksa == null sa hiçbişey yapma
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        emailText = findViewById(R.id.etEmail);
        passwordText = findViewById(R.id.etPassword);
        userAuth = FirebaseAuth.getInstance();


    }



    public void signIn(View v){

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        // girilen değerler boş değilse aşağıdakini gerçekleştir
        if(!email.equals("") && !password.equals("")){
            userAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(UserRegister.this,"Giriş başarılı",Toast.LENGTH_SHORT).show();
                                FirebaseUser currentUser = userAuth.getCurrentUser();
                                updateUI(currentUser);
                            } else {
                                Toast.makeText(UserRegister.this, "Giriş başarısız",Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        } else{
            // girilen değerler boşsa hata mesajı verelim
            Toast.makeText(this, "Eposta ve parola boş geçilemez", Toast.LENGTH_SHORT).show();
        }



    }

    public void signUp(View v){

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        // girilen değerler boş değilse aşağıdakini gerçekleştir
        if(!email.equals("") && !password.equals("")){
            userAuth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(UserRegister.this, "Kayıt başarılı", Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = userAuth.getCurrentUser();
                            updateUI(currentUser);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserRegister.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    });
        } else{
          // boşsa hata mesajı verelim
            Toast.makeText(this, "Eposta ve parola boş geçilemez", Toast.LENGTH_SHORT).show();
        }



    }



}
