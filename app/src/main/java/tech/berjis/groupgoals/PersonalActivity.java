package tech.berjis.groupgoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends AppCompatActivity {

    RecyclerView transactions;
    List<Transactions> listData;
    TransactionsAdapter transactionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        transactions = findViewById(R.id.transactions);

        loadTransactions();
    }


    private void loadTransactions(){
        listData = new ArrayList<>();
        listData.clear();
        transactions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listData.add(new Transactions(100,100, 100,"success","","deposit","",""));
        listData.add(new Transactions(100,100, 100,"error","","withdraw","",""));
        listData.add(new Transactions(100,100, 100,"cancelled","","withdraw","",""));
        listData.add(new Transactions(100,100, 100,"success","","deposit","",""));
        listData.add(new Transactions(100,100, 100,"success","","withdraw","",""));
        listData.add(new Transactions(100,100, 100,"error","","deposit","",""));
        transactionsAdapter = new TransactionsAdapter(listData, "complete");
        transactions.setAdapter(transactionsAdapter);
    }
}
