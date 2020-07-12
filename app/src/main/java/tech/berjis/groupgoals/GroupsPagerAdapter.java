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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
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

        View mView = LayoutInflater.from(mContext).inflate(R.layout.group_card, container, false);
        final GroupsList ld = listData.get(position);

        /*ImageView imgSlide = mView.findViewById(R.id.intro_img);
        TextView title = mView.findViewById(R.id.intro_title);
        TextView description = mView.findViewById(R.id.intro_description);

        title.setText(listData.get(position).getTitle());
        description.setText(listData.get(position).getDescription());
        imgSlide.setImageResource(listData.get(position).getScreenImg());*/

        TextView groupName = mView.findViewById(R.id.groupName);
        groupName.setText(ld.getName());

        loadGroupData(ld, mView);


        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupIntent = new Intent(mContext, GroupActivity.class);
                Bundle groupBundle = new Bundle();
                groupBundle.putString("group_id", ld.getGroup_id());
                groupIntent.putExtras(groupBundle);
                mContext.startActivity(groupIntent);
            }
        });

        container.addView(mView);

        return mView;


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

    private void loadGroupData(GroupsList ld, View mView){

        final TextView groupPurpose = mView.findViewById(R.id.groupPurpose);
        final TextView groupGoal = mView.findViewById(R.id.groupGoal);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Groups").child(ld.getGroup_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currency = snapshot.child("currency").getValue().toString();
                long goal = Long.parseLong(snapshot.child("goal").getValue().toString());
                String purpose = snapshot.child("description").getValue().toString();
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                groupPurpose.setText(purpose);
                groupGoal.setText(currency+" "+formatter.format(goal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}