package com.technominds.lecture.socialmediaapp.CustomAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.MessagePattern;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.technominds.lecture.socialmediaapp.CustomModels.PostModel;
import com.technominds.lecture.socialmediaapp.R;

import java.util.List;

public class PostRecyclerView extends RecyclerView.Adapter<PostRecyclerView.Myholder> {

    List<PostModel> list;
    Context ctx;
    boolean click=false;
    DatabaseReference lref;

    public PostRecyclerView(List<PostModel> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.recycler_post_item,parent,false);
        return new Myholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        Glide.with(ctx).load(list.get(position).getPost_image()).into(holder.rp_img);
        holder.rp_title.setText(list.get(position).getTitle());
        holder.rp_desc.setText(list.get(position).getDesc());
        holder.rp_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareimage(holder,position);
            }
        });

        holder.rp_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click=true;
                likeimage( holder, position);
              }
        });

        String postkey=list.get(position).getKey();
        String uid=list.get(position).getUid();
        holder.checklikedstatus(postkey,uid);
    }

    private void likeimage(Myholder holder,int position) {
        lref=FirebaseDatabase.getInstance().getReference("posts");
        lref.child(list.get(position).getKey()).child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(click) {
                    if (snapshot.hasChild(list.get(position).getUid())) {
                        lref.child(list.get(position).getKey()).child("likes").child(list.get(position).getUid()).removeValue();
                    } else {
                        lref.child(list.get(position).getKey()).child("likes").child(list.get(position).getUid()).setValue(true);

                    }
                    click=false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void shareimage( Myholder holder,int position)
    {
        BitmapDrawable drawable=(BitmapDrawable) holder.rp_img.getDrawable();
        Bitmap bitmap=drawable.getBitmap();
        String path= MediaStore.Images.Media.insertImage(ctx.getContentResolver(),bitmap,"title","desc");
        Intent i=new Intent(Intent.ACTION_SEND);

        Uri uri=Uri.parse(path);
        i.putExtra(Intent.EXTRA_STREAM,uri);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_TEXT,list.get(position).getTitle()+"\n"+list.get(position).getDesc());
        ctx.startActivity(Intent.createChooser(i,"Share via"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myholder extends RecyclerView.ViewHolder{
        ImageView rp_img,rp_like,rp_share;
        TextView rp_title,rp_desc,rp_count;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            rp_img=itemView.findViewById(R.id.rp_img);
            rp_like=itemView.findViewById(R.id.rp_like);
            rp_share=itemView.findViewById(R.id.rp_share);

            rp_title=itemView.findViewById(R.id.rp_title);
            rp_desc=itemView.findViewById(R.id.rp_desc);
            rp_count=itemView.findViewById(R.id.rp_count);

        }

        public void checklikedstatus(String postkey,String uid){
            lref=FirebaseDatabase.getInstance().getReference("posts");
            lref.child(postkey).child("likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int c=(int) snapshot.getChildrenCount();
                    rp_count.setText(c+" liked");
                    if(snapshot.hasChild(uid))
                    {
                        rp_like.setImageResource(R.drawable.ic_liked);
                    }
                    else
                    {
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
