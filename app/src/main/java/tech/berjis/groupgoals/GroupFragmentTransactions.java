package tech.berjis.groupgoals;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupFragmentTransactions extends Fragment {
    private Context mContext;
    private String group;
    private List<Transactions> listData;
    private TransactionsAdapter transactionsAdapter;
    private RecyclerView transactions;

    public GroupFragmentTransactions(Context mContext, String group) {
        this.mContext = mContext;
        this.group = group;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_group_transactions, container, false);

        transactions = view.findViewById(R.id.transactions);
        loadTransactions();
        return view;
    }

    private void loadTransactions(){
        long time_start = System.currentTimeMillis() / 1000L;
        long end_time = System.currentTimeMillis() / 1000L;
        listData = new ArrayList<>();
        listData.clear();
        transactions.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        listData.add(new Transactions(100,end_time - 120, time_start - 120,"success","","deposit","",""));
        listData.add(new Transactions(100,end_time - 121, time_start - 120,"error","","withdraw","",""));
        listData.add(new Transactions(100,end_time - 122, time_start - 120,"cancelled","","withdraw","",""));
        listData.add(new Transactions(100,end_time - 123, time_start - 120,"success","","deposit","",""));
        listData.add(new Transactions(100,end_time - 124, time_start - 120,"success","","withdraw","",""));
        listData.add(new Transactions(100,end_time - 125, time_start - 120,"error","","deposit","",""));
        transactionsAdapter = new TransactionsAdapter(listData, "group", "Kshs");
        transactions.setAdapter(transactionsAdapter);
    }
}
