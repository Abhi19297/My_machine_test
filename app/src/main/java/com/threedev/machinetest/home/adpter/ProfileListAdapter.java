package com.threedev.machinetest.home.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.threedev.machinetest.R;
import com.threedev.machinetest.home.model.HomeModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<HomeModel.Data> list;
    List<HomeModel.Data> mFilterlist ;



    public ProfileListAdapter(Context context, ArrayList<HomeModel.Data> list) {
        this.context = context;
        this.list=list;
        this.mFilterlist=list;



    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.profile_details_list_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {



        Glide
                .with(context)
                .load(mFilterlist.get(position).getAvatar())
                //.placeholder(R.drawable.splash_img)
                .into(holder.ivProfile);

        holder.txtName.setText(mFilterlist.get(position).getFirst_name()+" "+mFilterlist.get(position).getLast_name());
        holder.txtEmail.setText(mFilterlist.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return mFilterlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView txtName,txtEmail;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile=itemView.findViewById(R.id.iv_profile);
            txtName=itemView.findViewById(R.id.txt_name);
            txtEmail=itemView.findViewById(R.id.txt_email);




        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilterlist = list;
                } else {

                    ArrayList<HomeModel.Data> filterData = new ArrayList<>();
                    for (HomeModel.Data row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row!=null && (row.getFirst_name()+row.getLast_name()).toLowerCase().contains(charString.toLowerCase())) {

                            filterData.add(row);
                        }
                    }

                    mFilterlist = filterData;
                    if(filterData.size()==0){
                        Toast.makeText(context,"No data found.",Toast.LENGTH_SHORT).show();
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                /*  list = (ArrayList<FoodTripResponse.FoodStripData>) results.values;*/
                mFilterlist = (ArrayList<HomeModel.Data>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
