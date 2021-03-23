package com.example.firestore.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestore.DetailsActivity;
import com.example.firestore.Model.Request;
import com.example.firestore.databinding.RecylersingleviewBinding;

import java.util.List;

public class RecyclerViewAdapterRequest extends RecyclerView.Adapter<RecyclerViewAdapterRequest.ViewHolder> {

    public Context context;
    private final List<Request> items;
    private RecylersingleviewBinding recylersingleviewBinding;
    private String donner_id;


    public RecyclerViewAdapterRequest(final Context context, final List<Request> items,String donner_id) {
        this.context = context;
        this.items = items;
        this.donner_id=donner_id;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterRequest.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        RecylersingleviewBinding binding=RecylersingleviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);


        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterRequest.ViewHolder holder, int position) {

        Request request=items.get(position);

        holder.recylersingleviewBindings.BloodGroupTextViews.setText(request.getRequested_blood());
        holder.recylersingleviewBindings.FromdateTextView.setText(request.getRequested_time_frame_from());
        holder.recylersingleviewBindings.ToDateTextView.setText(request.getRequested_time_frame_to());
        holder.recylersingleviewBindings.LocationTextView.setText(request.getPreferred_location());
        holder.recylersingleviewBindings.ClickToSeeTextView.setOnClickListener(v ->{
            Intent intent=new Intent(context, DetailsActivity.class);
            intent.putExtra("donner_id",request.getDonner_id());
            intent.putExtra("request_id",request.getRecipient_id());
            intent.putExtra("mainid",donner_id);
            context.startActivity(intent);


          });

    }


    @Override
    public int getItemCount() {
        return items.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecylersingleviewBinding recylersingleviewBindings;

        public ViewHolder(@NonNull RecylersingleviewBinding itemView) {
            super(itemView.getRoot());

            recylersingleviewBindings=itemView;

        }
    }


}
