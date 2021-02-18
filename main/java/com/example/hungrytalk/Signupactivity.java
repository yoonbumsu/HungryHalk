package com.example.hungrytalk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import model.User;


public class Signupactivity extends AppCompatActivity {

    private ImageView profile;
    private Uri imageUri;
    private static final int PICK_FORM_ALBUM = 10;
    private StorageReference profileImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        final String TAG = "Signupactivity";

        final EditText email, password, passwordcheck, name;
        final FirebaseAuth mAuth;
        final ProgressBar progressBar;
        final FirebaseDatabase database;
        database = FirebaseDatabase.getInstance();


        profile = findViewById(R.id.Signupactivity_imageview_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FORM_ALBUM);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.nameedittext);
        email = findViewById(R.id.emailedittext);
        password = findViewById(R.id.passwordedittext);
        Button signupbutton = findViewById(R.id.signupbutton);
        passwordcheck = findViewById(R.id.passwordcheckedittext);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stPassword = password.getText().toString();
                String stEmail = email.getText().toString();
                String stPassword2 = passwordcheck.getText().toString();
                if (stEmail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "비밀번호을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stPassword2.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "입력하신 비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageUri == null){
                    Toast.makeText(getApplicationContext(), "이미지를 등록해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // progressBar.setVisibility(View.VISIBLE);  / 대기로딩중
                //Toast.makeText(getApplicationContext(),"Email : "+stEmail+",password : "+stEmail,Toast.LENGTH_LONG).show();

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(Signupactivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                FirebaseStorage.getInstance().getReference().child("userImaegs").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                                        Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                        while(!imageUrl.isComplete());

                                        User user = new User();
                                        user.userName = name.getText().toString();
                                        user.profileImageUrl = imageUrl.getResult().toString();
                                        user.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Signupactivity.this,"회원가입이 완료되었습니다",Toast.LENGTH_SHORT).show();
                                                Signupactivity.this.finish();
                                            }
                                        });

                                    }
                                });
                            }
                        });


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FORM_ALBUM && resultCode == RESULT_OK) {
            profile.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }
}



