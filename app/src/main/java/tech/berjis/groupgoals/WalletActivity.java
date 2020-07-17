package tech.berjis.groupgoals;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class WalletActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dbRef;

    TextView currency, username;
    CircleImageView userimage;
    String c_name = "", c_code = "", c_symbol = "", UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initLayouts();
        loadUserDetails();
        staticOnClicks();
    }

    private void initLayouts() {
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.keepSynced(true);

        UID = mAuth.getCurrentUser().getUid();

        currency = findViewById(R.id.currency);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
    }

    private void loadUserDetails() {
        dbRef.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String symbol = snapshot.child("currency_symbol").getValue().toString();
                String code = snapshot.child("currency_code").getValue().toString();
                String name = snapshot.child("user_name").getValue().toString();
                String image = snapshot.child("user_image").getValue().toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    currency.setText(Html.fromHtml(symbol + " <small>(" + code + ")</small>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    currency.setText(Html.fromHtml(symbol + " <small>(" + code + ")</small>"));
                }
                username.setText(name);
                Glide.with(WalletActivity.this).load(image).thumbnail(0.25f).into(userimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void staticOnClicks() {
        currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            currency.setText(Html.fromHtml(symbol + " <small>(" + code + ")</small>", Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            currency.setText(Html.fromHtml(symbol + " <small>(" + code + ")</small>"));
                        }
                        c_name = name;
                        c_code = code;
                        c_symbol = symbol;

                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
            }
        });
    }
}
