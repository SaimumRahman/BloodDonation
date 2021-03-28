package com.example.firestore.UI;

import android.content.Context;
import android.service.autofill.UserData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestore.Model.Donation_History;
import com.example.firestore.databinding.NotificationitemviewBinding;

import java.util.List;

public class RecyclerAdapterNotification extends RecyclerView.Adapter<RecyclerAdapterNotification.ViewHolder> {

    private Context context;
    private List<Donation_History>donation_historyList;

    public RecyclerAdapterNotification(final Context context, final List<Donation_History> donation_historyList) {
        this.context = context;
        this.donation_historyList = donation_historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NotificationitemviewBinding notificationitemviewBinding=NotificationitemviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ViewHolder(notificationitemviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Donation_History donation_history=donation_historyList.get(position);

        holder.binding.donationDateTv.setText(donation_history.getDonation_date());
        holder.binding.bloodReciepientNoTv.setText(donation_history.getReciepient_id());

    }

    @Override
    public int getItemCount() {
        return donation_historyList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private NotificationitemviewBinding binding;
        public ViewHolder(@NonNull NotificationitemviewBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
}
