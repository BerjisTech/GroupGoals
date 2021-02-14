package tech.berjis.groupgoals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    TextView edit_profile_txt, change_phone_txt, terms_of_use_txt, logout_txt, notifications_txt;
    ImageView edit_profile_btn, change_phone_btn, back, terms_of_use_btn, logout_btn, notifications_btn, topContentImage;

    FirebaseAuth mAuth;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.keepSynced(true);

        edit_profile_txt = findViewById(R.id.edit_profile_txt);
        edit_profile_btn = findViewById(R.id.edit_profile_btn);
        change_phone_txt = findViewById(R.id.change_phone_txt);
        change_phone_btn = findViewById(R.id.change_phone_btn);
        terms_of_use_btn = findViewById(R.id.terms_of_use_btn);
        terms_of_use_txt = findViewById(R.id.terms_of_use_txt);
        notifications_txt = findViewById(R.id.notifications_txt);
        notifications_btn = findViewById(R.id.notifications_btn);
        topContentImage = findViewById(R.id.topContentImage);
        logout_btn = findViewById(R.id.logout_btn);
        logout_txt = findViewById(R.id.logout_txt);
        back = findViewById(R.id.back);

        staticOnclick();
        loadContent();
    }

    private void staticOnclick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.super.finish();
            }
        });

        edit_profile_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
            }
        });

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
            }
        });

        change_phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePhoneNumber.class));
            }
        });

        change_phone_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePhoneNumber.class));
            }
        });

        terms_of_use_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, TermsActivity.class));
            }
        });

        terms_of_use_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, TermsActivity.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        logout_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        notifications_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, NotificationsActivity.class));
            }
        });

        notifications_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, NotificationsActivity.class));
            }
        });

        topContentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, FinancialEducation.class));
            }
        });
    }

    private void loadContent() {
        dbRef.child("FinEd").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                    if (npsnapshot.hasChildren()) {

                        TextView topContentText = findViewById(R.id.topContentText);
                        TextView topContentTitle = findViewById(R.id.topContentTitle);

                        String text = Objects.requireNonNull(npsnapshot.child("content").getValue()).toString();
                        String title = Objects.requireNonNull(npsnapshot.child("topic").getValue()).toString();
                        String image = Objects.requireNonNull(npsnapshot.child("image").getValue()).toString();

                        if (text.length() > 99) {
                            text = text.substring(0, 100) + "...";
                        }
                        if (title.length() > 20) {
                            title = title.substring(0, 21) + "...";
                        }

                        topContentTitle.setText(title);
                        topContentText.setText(text);
                        Picasso.get().load(image).placeholder(R.drawable.img_one).into(topContentImage);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
