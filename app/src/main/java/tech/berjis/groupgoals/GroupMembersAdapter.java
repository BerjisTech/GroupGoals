package tech.berjis.groupgoals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.ViewHolder> {

    private List<GroupMembers> listData;
    private Context mContext;

    GroupMembersAdapter(Context mContext, List<GroupMembers> listData) {
        this.mContext = mContext;
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_members, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final GroupMembers ld = listData.get(position);


        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        long time = ld.getJoined_on() * 1000;
        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        String ago = prettyTime.format(new Date(time));

        holder.memberSince.setText("Joined " + ago);

        dbRef.child("Users").child(ld.getMember_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("user_name").exists() && !snapshot.child("user_name").getValue().toString().equals("")) {
                    holder.memberName.setText(Objects.requireNonNull(snapshot.child("user_name").getValue()).toString());
                }
                if (snapshot.child("user_image").exists() && !snapshot.child("user_image").getValue().toString().equals("")) {
                    Glide.with(mContext).load(Objects.requireNonNull(snapshot.child("user_image").getValue()).toString()).thumbnail(0.25f).into(holder.memberImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context mContext = holder.itemView.getContext();
                Intent d_c = new Intent(mContext, DMActivity.class);
                Bundle d_b = new Bundle();

                d_b.putString("user", ld.getMember_id());
                d_c.putExtras(d_b);
                mContext.startActivity(d_c);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView memberImage;
        TextView memberName, memberSince;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            memberImage = itemView.findViewById(R.id.memberImage);
            memberName = itemView.findViewById(R.id.memberName);
            memberSince = itemView.findViewById(R.id.memberSince);
        }
    }
}
