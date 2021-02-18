package com.example.hungrytalk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;

import write.WriteInfo;

public class Write extends AppCompatActivity {
    private Uri imageUri;
    int REQUEST_IMAGE_CODE = 1001;
    private static final int PICK_FORM_ALBUM = 10;
    private FirebaseAuth auth;
    private ImageView imageUp;

    private FirebaseStorage storage;
    private FirebaseUser user;
    private static final String TAG = "Write";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        Button check = findViewById(R.id.check);
        findViewById(R.id.imageUp);
        imageUp = findViewById(R.id.imageUp);


        imageUp.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent(Intent.ACTION_PICK);
                                           intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                           startActivityForResult(intent, PICK_FORM_ALBUM);
                                       }
                                   });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.check:
                        profileUpdate();
                        break;



                }

            }



            private void profileUpdate() {
                final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
                final String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();
             //   final String imageup = ((ImageView)findViewById(R.id.imageUp).get
             //   @SuppressLint("WrongViewCast") final String image  =((EditText )findViewById(R.id.imageUp).geti)
                if (title.length() > 0 && contents.length() > 0) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    WriteInfo writeInfo = new WriteInfo(title, contents, user.getUid());
                    uploader(writeInfo);

                } else {

                    //  startToast("회원정보를 입력해주세요.");
                }
            }

            private void uploader(WriteInfo writeInfo) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("posts").add(writeInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }


        });
    }
   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FORM_ALBUM && resultCode == RESULT_OK) {
            imageUp.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }}


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FORM_ALBUM && resultCode == RESULT_OK) {
            storage = FirebaseStorage.getInstance();

            System.out.println(getPath(data.getData()));
            StorageReference storageRef = storage.getReference();

            Uri file = Uri.fromFile(new File(getPath(data.getData())));
            StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
        }
    }
    public String getPath(Uri uri){
        String []  proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor =cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }}
*/