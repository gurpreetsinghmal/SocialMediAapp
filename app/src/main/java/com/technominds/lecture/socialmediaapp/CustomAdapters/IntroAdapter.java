package com.technominds.lecture.socialmediaapp.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.technominds.lecture.socialmediaapp.CustomModels.IntroModel;
import com.technominds.lecture.socialmediaapp.R;

import java.util.List;

public class IntroAdapter extends PagerAdapter {

    List<IntroModel> list;
    Context ctx;

    public IntroAdapter(List<IntroModel> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.intro_vp,container,false);
        ImageView imv=v.findViewById(R.id.intro_vp_imv);
        TextView title=v.findViewById(R.id.intro_vp_title);
        imv.setImageResource(list.get(position).getIntro_image());
        title.setText(list.get(position).getIntro_title());
        container.addView(v);
        return v;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
