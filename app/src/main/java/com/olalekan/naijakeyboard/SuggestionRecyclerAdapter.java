package com.olalekan.naijakeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.olalekan.naijakeyboard.defaults.Constants;

import java.util.ArrayList;

public class SuggestionRecyclerAdapter extends RecyclerView.Adapter<SuggestionRecyclerAdapter.SuggestionViewHolder>{

    Context mContext;
    ArrayList<String> mSuggestions;
    LayoutInflater mLayoutInflater;
    RecyclerViewClicked mRecyclerViewClicked;

    public SuggestionRecyclerAdapter(Context context, ArrayList<String> suggestions){
        mContext = context;
        mSuggestions = suggestions;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.single_suggestion, parent,false);
        return new SuggestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        holder.suggestionTextView.setText(mSuggestions.get(position));
    }

    @Override
    public int getItemCount() {
        return mSuggestions.size();
    }

    public class SuggestionViewHolder extends RecyclerView.ViewHolder{
        public final CardView suggestionCardView;
        public final TextView suggestionTextView;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            suggestionCardView = itemView.findViewById(R.id.cardViewSuggestion);
            suggestionTextView = itemView.findViewById(R.id.textViewSuggestion);

            SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.input_method_preference), Context.MODE_PRIVATE);
            float keyOpacity = sharedPreferences.getFloat(mContext.getString(R.string.key_opacity_preference), Constants.DEFAULT_KEYBOARD_OPACITY_ALPHA);
            suggestionCardView.setAlpha(keyOpacity);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mRecyclerViewClicked != null) {
                        mRecyclerViewClicked.recyclerViewClicked(suggestionTextView.getText().toString());
                    } else
                        Log.e("RecyclerViewClicked", "onClick: Suggestion Recycler view", new Exception("recycler view error"));
                }
            };
            suggestionCardView.setOnClickListener(onClickListener);


        }

    }

    public void addToSuggestions(String word){
        mSuggestions.add(word);
        notifyDataSetChanged();
    }

    public void changeSuggestions(ArrayList<String> words){
        mSuggestions = words;
        notifyDataSetChanged();
    }

    public void setRecyclerViewClickedListener(RecyclerViewClicked recyclerViewClicked){
        mRecyclerViewClicked = recyclerViewClicked;
    }

    interface RecyclerViewClicked{
        void recyclerViewClicked(String suggestion);
    }
}
