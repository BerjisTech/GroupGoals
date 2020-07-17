package tech.berjis.groupgoals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonalActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dbRef;

    RecyclerView transactions;
    List<Transactions> listData;
    TransactionsAdapter transactionsAdapter;

    TextView deposit;
    String UID, uPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        initLayout();
        staticOnclicks();
        loadTransactions();
    }

    private void initLayout() {
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.keepSynced(true);

        UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        uPhone = mAuth.getCurrentUser().getPhoneNumber();

        deposit = findViewById(R.id.deposit);
        transactions = findViewById(R.id.transactions);
    }

    private void staticOnclicks() {
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalActivity.this, WalletActivity.class));
            }
        });
    }


    private void loadTransactions() {
        listData = new ArrayList<>();
        listData.clear();
        transactions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listData.add(new Transactions(100, 100, 100, "success", "", "deposit", "", ""));
        listData.add(new Transactions(100, 100, 100, "error", "", "withdraw", "", ""));
        listData.add(new Transactions(100, 100, 100, "cancelled", "", "withdraw", "", ""));
        listData.add(new Transactions(100, 100, 100, "success", "", "deposit", "", ""));
        listData.add(new Transactions(100, 100, 100, "success", "", "withdraw", "", ""));
        listData.add(new Transactions(100, 100, 100, "error", "", "deposit", "", ""));
        transactionsAdapter = new TransactionsAdapter(listData, "complete");
        transactions.setAdapter(transactionsAdapter);
    }
}
