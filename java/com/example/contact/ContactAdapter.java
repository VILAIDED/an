package com.example.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    public ArrayList<Contact> contacts;
    private ItemClickListener itemClickListener;

    private String[] colorArr;
    public ContactAdapter(ArrayList<Contact> contacts,ItemClickListener itemClickListener) {
        this.contacts = contacts;
        this.itemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(view,itemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {

        String name = contacts.get(position).getName();
        holder.tvName.setText(name);
        byte[] bImg = contacts.get(position).getImage();
        if(bImg != null){
            Bitmap b = BitmapFactory.decodeByteArray(bImg, 0, bImg.length);
            holder.imgView.setImageBitmap(b);
        }
        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent (view.getContext(),AddContact.class);

            }
        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemClickListener mOnClickListener;
        public TextView tvName;
        public ImageView imgView;
        public ViewHolder(View view,ItemClickListener mOnClickListener) {

            super(view);
//            colorArr =
            tvName = view.findViewById(R.id.tv_name);
            imgView = view.findViewById(R.id.ic_avatar);
            this.mOnClickListener = mOnClickListener;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(v,getAdapterPosition());
                }
            });
        }
        public void setOnItemClickListener(ItemClickListener itemClickListener) {
            this.mOnClickListener = itemClickListener;
        }


    }

}

