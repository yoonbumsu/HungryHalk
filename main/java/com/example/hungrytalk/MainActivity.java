package com.example.hungrytalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    EditText etId, etPassword;
    TextView tv_error_email;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressBar);
        etId = findViewById(R.id.editname);
        etPassword = findViewById(R.id.etPassword);
        tv_error_email = findViewById(R.id.tv_error_email);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        etId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    tv_error_email.setText("이메일 형식으로 입력해주세요.");    // 경고 메세지
                    etId.setBackgroundResource(R.drawable.redwrite_edittext);  // 적색 테두리 적용
                } else {
                    tv_error_email.setText("");         //에러 메세지 제거
                    etId.setBackgroundResource(R.drawable.write_edittext);
                }


                Button btnLogin = findViewById(R.id.checkbutton);
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final String stEmail = etId.getText().toString();
                        String stPassword = etPassword.getText().toString();

                        if (stEmail.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (stPassword.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "비밀번호을 입력해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.signInWithEmailAndPassword(stEmail, stPassword)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String stUserEmail = user.getEmail();
                                            String stUserName = user.getDisplayName();
                                            Log.d(TAG, "stUserEmail: " + stUserEmail + ",stUserName : " + stUserName);
                                            Intent in = new Intent(MainActivity.this, BottomNavi.class); //로그인시
                                            in.putExtra("email", stEmail);
                                            startActivity(in);

                                            //updateUI(user);

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(MainActivity.this, "회원정보가 올바르지 않습니다.",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                        //passPushTokenToServer();
                                        // ...
                                    }
                                });


                    }
                });

                Button btnRegister = findViewById(R.id.btnRegister);
                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, Signupactivity.class);
                        startActivity(intent);
                /* String stPassword = etPassword.getText().toString();
                String stEmail =etId.getText().toString();


                if (stEmail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                     }
                    if (stPassword.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "비밀번호을 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }


                progressBar.setVisibility(View.VISIBLE);
              //Toast.makeText(getApplicationContext(),"Email : "+stEmail+",password : "+stEmail,Toast.LENGTH_LONG).show();
                mAuth.createUserWithEmailAndPassword(stEmail, stPassword)
                        .addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task){
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                    //회원가입 시 데이터 데이터베이스로 공유
                                    DatabaseReference myRef = database.getReference("message").child(user.getUid());

                                    Hashtable<String, String> numbers
                                            = new Hashtable<String, String>();
                                    numbers.put("email", user.getEmail());
                                    myRef.setValue(numbers);
                                    Toast.makeText(MainActivity.this,"회원가입 완료",Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
                    }


                });
                */
                    }

                });
                passPushTokenToServer();
            }

            void passPushTokenToServer() {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String token = FirebaseInstanceId.getInstance().getToken();
                Map<String, Object> map = new HashMap<>();
                map.put("pushToken", token);
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
            }
        });
    }
}




