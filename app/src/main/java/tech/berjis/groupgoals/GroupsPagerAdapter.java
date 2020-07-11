package tech.berjis.groupgoals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class GroupsPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<GroupsList> listData;

    GroupsPagerAdapter(Context mContext, List<GroupsList> listData) {
        this.mContext = mContext;
        this.listData = listData;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View layoutScreen = LayoutInflater.from(mContext).inflate(R.layout.group_card, container, false);
        final GroupsList ld = listData.get(position);

        /*ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
        TextView title = layoutScreen.findViewById(R.id.intro_title);
        TextView description = layoutScreen.findViewById(R.id.intro_description);

        title.setText(listData.get(position).getTitle());
        description.setText(listData.get(position).getDescription());
        imgSlide.setImageResource(listData.get(position).getScreenImg());*/

        TextView groupName = layoutScreen.findViewById(R.id.groupName);
        groupName.setText(ld.getName());


        layoutScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupIntent = new Intent(mContext, GroupActivity.class);
                Bundle groupBundle = new Bundle();
                groupBundle.putString("group_id", ld.group_id);
                groupIntent.putExtras(groupBundle);
                mContext.startActivity(groupIntent);
            }
        });

        container.addView(layoutScreen);

        return layoutScreen;


    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }
}