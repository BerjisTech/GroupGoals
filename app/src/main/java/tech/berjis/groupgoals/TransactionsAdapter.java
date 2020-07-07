package tech.berjis.groupgoals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    private List<Transactions> listData;
    private String type;

    public TransactionsAdapter(List<Transactions> listData, String type) {
        this.listData = listData;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Transactions ld = listData.get(position);

        if (type.equals("complete")) {
            if (ld.getStatus().equals("success")) {
                holder.indicator.setBackgroundResource(R.drawable.indicator_good);
            }
            if (ld.getStatus().equals("error")) {
                holder.indicator.setBackgroundResource(R.drawable.indicator_warning);
            }
            if (ld.getStatus().equals("cancelled")) {
                holder.indicator.setBackgroundResource(R.drawable.indicator_bad);
            }
        }

        if (type.equals("wallet")) {
            if (ld.getStatus().equals("success")) {
                //holder.narration.setTextColor(Color.parseColor("#007e33"));
                holder.indicator.setBackgroundColor(Color.parseColor("#00c851"));
            }
            if (ld.getStatus().equals("error")) {
                //holder.narration.setTextColor(Color.parseColor("#cc0000"));
                holder.indicator.setBackgroundColor(Color.parseColor("#ff4444"));
            }
            if (ld.getStatus().equals("cancelled")) {
                //holder.narration.setTextColor(Color.parseColor("#ff8800"));
                holder.indicator.setBackgroundColor(Color.parseColor("#ffbb33"));
            }
        }

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(0);
        String output = nf.format(ld.getAmount());

        if (ld.getType().equals("deposit")) {
            holder.amount.setText("+kshs " + output);
            holder.amount.setTextColor(Color.parseColor("#18a3fe"));
        }
        if (ld.getType().equals("withdraw")) {
            holder.amount.setText("-kshs " + output);
            holder.amount.setTextColor(Color.parseColor("#FE18A3"));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context tContext = holder.mView.getContext();
                Intent tIntent = new Intent(tContext, GroupTransactionActivity.class);
                Bundle tBundle = new Bundle();
                tBundle.putString("t_id", ld.getText_ref());
                tIntent.putExtras(tBundle);
                tContext.startActivity(tIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, amount;
        View mView, indicator;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            amount = itemView.findViewById(R.id.amount);
            indicator = itemView.findViewById(R.id.indicator);
            mView = itemView;
        }
    }
}
