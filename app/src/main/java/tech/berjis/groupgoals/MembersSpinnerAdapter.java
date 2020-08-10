package tech.berjis.groupgoals;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/***** Adapter class extends with ArrayAdapter ******/
public class MembersSpinnerAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList data;
    public Resources res;
    private GroupMembers tempValues = null;
    private LayoutInflater inflater;

    /*************  MembersSpinnerAdapter Constructor *****************/
    public MembersSpinnerAdapter(
            GroupSettingsActivity activitySpinner,
            int textViewResourceId,
            ArrayList objects,
            Resources resLocal
    ) {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data = objects;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.custom_spinner_row, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (GroupMembers) data.get(position);

        TextView userName = row.findViewById(R.id.userName);
        TextView userId = row.findViewById(R.id.userId);

        if (position == 0) {
            // Default selected Spinner item
            userName.setText("");
        } else {
            // Set values for spinner each row
            loadUser(row, tempValues.getMember_id());
            Toast.makeText(activity, tempValues.getMember_id(), Toast.LENGTH_SHORT).show();
            userId.setText(tempValues.getMember_id());

        }

        return row;
    }

    private void loadUser(final View row, String member_id) {
        final TextView userName = row.findViewById(R.id.userName);
        final CircleImageView userImage = row.findViewById(R.id.userImage);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Users").child(member_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = Objects.requireNonNull(snapshot.child("user_name").getValue()).toString();
                String logo = Objects.requireNonNull(snapshot.child("user_image").getValue()).toString();

                userName.setText(name);
                Glide.with(getContext()).load(logo).thumbnail(0.25f).into(userImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}