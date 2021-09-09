package com.technominds.lecture.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.technominds.lecture.socialmediaapp.CustomModels.PostModel;
import com.technominds.lecture.socialmediaapp.CustomModels.UserModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddPostActivity extends AppCompatActivity {
    Uri resultUri;
    ImageView post_imv,p_img;
    EditText post_title,post_desc;
    TextView p_title,p_desc;

    FirebaseAuth mauth;
    StorageReference sref;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        post_imv=findViewById(R.id.post_imv);
        p_img=findViewById(R.id.p_img);

        post_title=findViewById(R.id.post_title);
        post_desc=findViewById(R.id.post_desc);

        p_title=findViewById(R.id.p_title);
        p_desc=findViewById(R.id.p_desc);

        mauth=FirebaseAuth.getInstance();
        sref= FirebaseStorage.getInstance().getReference("posts");
        dbref= FirebaseDatabase.getInstance().getReference("posts");

        post_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                p_title.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        post_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                p_desc.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void picimage(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                post_imv.setImageURI(resultUri);
                p_img.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void addpost(View view) {
        if(resultUri==null)
        {
            Toast.makeText(this, "Choose Image", Toast.LENGTH_SHORT).show();
        }
        else if(post_title.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
        }
        else if(post_desc.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //folder
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            String owneremail=mauth.getCurrentUser().getEmail();
            String uid=mauth.getCurrentUser().getUid();
            String key=""+System.currentTimeMillis();
            sref.child(key).putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            PostModel p= new PostModel(post_title.getText().toString(),
                                    post_desc.getText().toString(),uri.toString(),owneremail,uid,key);

                            savedata(p);
                        }
                    });



                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setProgress((int) progress);
                    progressDialog.setMessage("Uploading :"+(progress)+" %");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void savedata(PostModel m) {
        dbref.child(m.getKey()).setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddPostActivity.this, "Successfully Posted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}