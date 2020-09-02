package tech.berjis.groupgoals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MembersSpinnerAdapter extends ArrayAdapter<GroupMembers> {

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    MembersSpinnerAdapter(Context context, ArrayList<GroupMembers> countryList) {
        super(context, 0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_spinner_row, parent, false
            );
        }
        final ImageView userImage = convertView.findViewById(R.id.userImage);
        final TextView userName = convertView.findViewById(R.id.userName);
        final TextView userId = convertView.findViewById(R.id.userId);

        GroupMembers ld = getItem(position);
        if (ld != null) {
            dbRef.child("Users").child(ld.getMember_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = Objects.requireNonNull(snapshot.child("user_name").getValue()).toString();
                    String image = Objects.requireNonNull(snapshot.child("user_image").getValue()).toString();
                    String id = Objects.requireNonNull(snapshot.child("user_id").getValue()).toString();

                    userName.setText(name);
                    userId.setText(id);
                    Glide.with(getContext()).load(image).thumbnail(0.25f).into(userImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return convertView;
    }
}