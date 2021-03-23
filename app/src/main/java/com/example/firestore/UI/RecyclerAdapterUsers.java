package com.example.firestore.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestore.Model.Request;
import com.example.firestore.Model.User;
import com.example.firestore.databinding.RecylersingleviewBinding;

import java.util.List;

public class RecyclerAdapterUsers extends RecyclerView.Adapter<RecyclerAdapterUsers.ViewHolder> {

    private Context context;
    private List<User>usersLists;
    private RecylersingleviewBinding recylersingleviewBinding;

    public RecyclerAdapterUsers(final Context context, final List<User> usersLists) {
        this.context = context;
        this.usersLists = usersLists;
    }

    @NonNull
    @Override
    public RecyclerAdapterUsers.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecylersingleviewBinding binding=RecylersingleviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);


        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterUsers.ViewHolder holder, int position) {

        User user=usersLists.get(position);

        holder.recylersingleviewBindings.BloodGroupTextViews.setText(user.getFirst_name() +" "+ user.getLast_name());
        holder.recylersingleviewBindings.LocationTextView.setText(user.getContact_number() +" "+ user.getAlternate_contact_number());
        holder.recylersingleviewBindings.FromdateTextView.setText(user.getReligion());
        holder.recylersingleviewBindings.ToDateTextView.setText(user.getBlood_group());
        holder.recylersingleviewBindings.TOTextView.setText(user.getWeight() + " Kg");
        holder.recylersingleviewBindings.ClickToSeeTextView.setText(user.getEmail() +" "+ user.getPresent_address());

    }

    @Override
    public int getItemCount() {
        return usersLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecylersingleviewBinding recylersingleviewBindings;
        public ViewHolder(@NonNull RecylersingleviewBinding itemView) {
            super(itemView.getRoot());
            recylersingleviewBindings=itemView;
        }
    }
}
