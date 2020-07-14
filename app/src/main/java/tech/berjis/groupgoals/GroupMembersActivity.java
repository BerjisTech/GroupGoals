package tech.berjis.groupgoals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class GroupMembersActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dbRef;

    String UID, group_id;

    TextView group_name, inviteButton;
    EditText memberPhone;
    CountryCodePicker memberPhoneCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);

        initLayout();
        onClicks();
        loadGroup();
        loadGroupMembers();
    }


    private void initLayout() {
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.keepSynced(true);

        UID = mAuth.getCurrentUser().getUid();

        group_name = findViewById(R.id.group_name);
        inviteButton = findViewById(R.id.inviteButton);
        memberPhone = findViewById(R.id.memberPhone);
        memberPhoneCode = findViewById(R.id.memberPhoneCode);

        Intent g_i = getIntent();
        Bundle g_b = g_i.getExtras();
        group_id = g_b.getString("group_id");
    }

    private void onClicks() {
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserExists();
            }
        });
    }

    private void checkUserExists() {
        final String code = memberPhoneCode.getSelectedCountryCode();
        final String phone = memberPhone.getText().toString();

        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("user_phone").equalTo("+"+code+phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Toast.makeText(GroupMembersActivity.this, "Invite sent", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(GroupMembersActivity.this, "User with +"+code+phone+" does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadGroup() {
        dbRef.child("Groups").child(group_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                group_name.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadGroupMembers() {
    }
}
