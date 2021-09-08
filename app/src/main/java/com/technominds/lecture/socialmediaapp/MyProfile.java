package com.technominds.lecture.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.technominds.lecture.socialmediaapp.CustomModels.UserModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class MyProfile extends AppCompatActivity {

    FirebaseAuth mauth;
    TextView email;
    ShapeableImageView imageview;
    Uri resultUri;
    EditText name;

    FirebaseStorage firebaseStorage;
    StorageReference mref;

    FirebaseDatabase db;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mauth=FirebaseAuth.getInstance();
        email=findViewById(R.id.profile_email);
        imageview=findViewById(R.id.profile_imv);
        name=findViewById(R.id.profile_name);

        firebaseStorage=FirebaseStorage.getInstance();
        db=FirebaseDatabase.getInstance();

        if(mauth.getCurrentUser().getEmail().isEmpty())
        {
            startActivity(new Intent(MyProfile.this,LoginActivity.class));
            finish();
        }
        else
        {
            email.setText(""+mauth.getCurrentUser().getEmail());
        }

    }

    public void chooseimage(View view) {
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
                imageview.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void saveprofile(View view) {
        if(resultUri==null)
        {
            Toast.makeText(this, "Choose Image", Toast.LENGTH_SHORT).show();
        }
        else if(name.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //folder
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            String user_id=mauth.getUid();
            mref=firebaseStorage.getReference("profiles/"+user_id);
            mref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            UserModel m=new UserModel(name.getText().toString(),user_id,mauth.getCurrentUser().getEmail()
                            ,uri.toString());
                            savedata(m);
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
                    Toast.makeText(MyProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void savedata(UserModel m) {
        dbref=db.getReference("users");
        dbref.child(m.getUid()).setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MyProfile.this, "Successfully Saved Profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MyProfile.this,HomeActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyProfile.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}