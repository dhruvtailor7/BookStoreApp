package com.example.bookstoreapp.adapater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapp.R;

import java.util.ArrayList;

public class LoginHistoryAdapter extends RecyclerView.Adapter<LoginHistoryAdapter.ViewHolder>{

    private ArrayList<String> loginHistory;

    public LoginHistoryAdapter(ArrayList<String> list){
        this.loginHistory=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.login_history_list,parent,false);
        return new LoginHistoryAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.login_history.setText(loginHistory.get(position));
    }

    @Override
    public int getItemCount() {
        return loginHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView login_history;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.login_history = itemView.findViewById(R.id.login_history);
        }
    }
}
