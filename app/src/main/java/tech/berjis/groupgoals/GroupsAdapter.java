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

import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    private List<GroupsList> listData;
    private Context mContext;

    GroupsAdapter(Context mContext, List<GroupsList> listData) {
        this.listData = listData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.groups, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GroupsList ld = listData.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupIntent = new Intent(mContext, GroupActivity.class);
                Bundle groupBundle = new Bundle();
                groupBundle.putString("group_id", ld.group_id);
                groupIntent.putExtras(groupBundle);
                mContext.startActivity(groupIntent);
            }
        });
        Random r = new Random();
        float random = 25 + r.nextFloat() * (100 - 25);
        holder.colorfulRingProgressView.setPercent(random);
        holder.groupName.setText(ld.name);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ColorfulRingProgressView colorfulRingProgressView;
        CircleImageView groupLogo;
        TextView groupName, groupProgress;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorfulRingProgressView = itemView.findViewById(R.id.colorfulRingProgressView);
            groupLogo = itemView.findViewById(R.id.groupLogo);
            groupName = itemView.findViewById(R.id.groupName);
            groupProgress = itemView.findViewById(R.id.groupProgress);
        }
    }
}
