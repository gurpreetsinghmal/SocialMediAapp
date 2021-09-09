package com.technominds.lecture.socialmediaapp.CustomAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technominds.lecture.socialmediaapp.CustomModels.PostModel;
import com.technominds.lecture.socialmediaapp.R;

import java.util.List;

public class PostRecyclerView extends RecyclerView.Adapter<PostRecyclerView.MyHolder> {

    List<PostModel> list;
    Context ctx;
    DatabaseReference lref;
    boolean click=false;

    public PostRecyclerView(List<PostModel> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.recycler_post_item,parent,false);

        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Glide.with(ctx).load(list.get(position).getPost_image()).into(holder.rp_img);
        holder.rp_title.setText(list.get(position).getTitle());
        holder.rp_desc.setText(list.get(position).getDesc());
        holder.rp_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable=(BitmapDrawable) holder.rp_img.getDrawable();
                Bitmap bitmap=drawable.getBitmap();
                String path= MediaStore.Images.Media.insertImage(ctx.getContentResolver(),bitmap,"title","desc");
                Uri uri=Uri.parse(path);
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM,uri);
                i.putExtra(Intent.EXTRA_TEXT,list.get(position).getTitle()+ "\n"+list.get(position).getDesc());
                ctx.startActivity(Intent.createChooser(i,"Share via"));
            }
        });

        holder.rp_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click=true;
                String post_key=list.get(position).getKey();
                String uid=FirebaseAuth.getInstance().getUid();
                lref= FirebaseDatabase.getInstance().getReference("posts");
                lref.child(post_key).child("likes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(click) {

                            if (snapshot.hasChild(uid)) {
                                lref.child(post_key).child("likes").child(uid).removeValue();
                            } else {
                                lref.child(post_key).child("likes").child(uid).setValue(true);
                            }
                            click=false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.checklikedstatus(list.get(position).getKey(), FirebaseAuth.getInstance().getUid());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView rp_img,rp_like,rp_share;
        TextView rp_title,rp_desc,rp_count;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rp_img=itemView.findViewById(R.id.rp_img);
            rp_like=itemView.findViewById(R.id.rp_like);
            rp_share=itemView.findViewById(R.id.rp_share);

            rp_title=itemView.findViewById(R.id.rp_title);
            rp_desc=itemView.findViewById(R.id.rp_desc);
            rp_count=itemView.findViewById(R.id.rp_count);
        }

        public void checklikedstatus(String post_key,String uid)
        {
            lref= FirebaseDatabase.getInstance().getReference("posts");
            lref.child(post_key).child("likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int c=(int)snapshot.getChildrenCount();
                        rp_count.setText(c+" Liked");
                        if(snapshot.hasChild(uid))
                        {
                            rp_like.setImageResource(R.drawable.ic_liked);
                        }
                        else {
                            rp_like.setImageResource(R.drawable.ic_disliked);
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }
}
