package com.olalekan.naijakeyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import keyboard_data.EmojisData;

public class EmojiTypeNavigationRecyclerAdapter extends RecyclerView.Adapter<EmojiTypeNavigationRecyclerAdapter.EmojiTypeNavigationViewHolder> {

    LayoutInflater mLayoutInflater;
    Context mContext;
    List<String> ListOfEmojiType;

    public EmojiTypeNavigationRecyclerAdapter(LayoutInflater layoutInflater, Context context, List<String> listOfEmojiType) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        ListOfEmojiType = EmojisData.emojiTypes;
    }

    @NonNull
    @Override
    public EmojiTypeNavigationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.single_emoji, parent, false);
        return new EmojiTypeNavigationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiTypeNavigationViewHolder holder, int position) {
        holder.typeTextView.setText(ListOfEmojiType.get(position));
    }

    @Override
    public int getItemCount() {
        return ListOfEmojiType.size();
    }

    class EmojiTypeNavigationViewHolder extends RecyclerView.ViewHolder{
        private final TextView typeTextView;

        public EmojiTypeNavigationViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.emojiTextView);
        }
    }
}
