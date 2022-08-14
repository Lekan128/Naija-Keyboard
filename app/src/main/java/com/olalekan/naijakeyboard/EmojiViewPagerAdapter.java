package com.olalekan.naijakeyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import keyboard_data.EmojisData;

public class EmojiViewPagerAdapter extends RecyclerView.Adapter<EmojiViewPagerAdapter.PagerAdapterViewHolder> {
 //emojis categoris list and hashmap
    EmojisData mEmojisData = EmojisData.getInstance();
    ArrayList<String > emojisType = EmojisData.emojiTypes;
    HashMap<String, String> emojisHashMap = EmojisData.emojiHashMap;


    Context mContext;
    LayoutInflater mLayoutInflater;
    public EmojiViewPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public PagerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PagerAdapterViewHolder(mLayoutInflater.inflate(R.layout.single_emoji_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PagerAdapterViewHolder holder, int position) {
        String type = emojisType.get(position);
        String emojisString = emojisHashMap.get(type);
        assert emojisString != null;
        String[] emojis =  emojisString.split(" ");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 5);
        holder.emojiRecuclerView.setAdapter(new com.olalekan.naijakeyboard.EmojiRecyclerAdapter(mContext, emojis, type));
        holder.emojiRecuclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public int getItemCount() {
        return emojisType.size();
    }

    public class PagerAdapterViewHolder extends RecyclerView.ViewHolder{
        final RecyclerView emojiRecuclerView;

        public PagerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            emojiRecuclerView = itemView.findViewById(R.id.emojisRecyclerView);

        }
    }

}
