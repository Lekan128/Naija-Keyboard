package com.olalekan.naijakeyboard;

import static com.olalekan.naijakeyboard.defaults.Constants.DEFAULT_SUGGESTION_SET;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

public class SuggestionInputRecyclerAdapter extends RecyclerView.Adapter<SuggestionInputRecyclerAdapter.SuggestionInputViewHolder> {
    Set<String> suggestions;
    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> suggestionArray;
//    Set<String> sug = new HashSet<>(suggestionArray);

    public SuggestionInputRecyclerAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.input_method_preference), Context.MODE_PRIVATE);
        suggestions = preferences.getStringSet(context.getString(R.string.saved_input_preference),
                DEFAULT_SUGGESTION_SET);
        suggestionArray = new ArrayList<>(suggestions);
    }

    @NonNull
    @Override
    public SuggestionInputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.single_suggestion_input, parent, false);
        return new SuggestionInputViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionInputViewHolder holder, int position) {
        holder.SuggestionInputText.setText(suggestionArray.get(position));
    }

    @Override
    public int getItemCount() {
        return suggestionArray.size();
    }

    public Set<String> getSuggestion(){
        return suggestions;
    }

    public void addSuggestion(String word){
        suggestions.add(word);
        suggestionArray.add(word);
//        notifyDataSetChanged();
        notifyItemInserted(suggestionArray.indexOf(word));
    }

    class SuggestionInputViewHolder extends RecyclerView.ViewHolder{

        private final TextView SuggestionInputText;
        private final ImageView SuggestionRemoveImage;



        public SuggestionInputViewHolder(@NonNull View itemView) {
            super(itemView);
            SuggestionInputText = itemView.findViewById(R.id.suggestionInputTextView);
            SuggestionRemoveImage = itemView.findViewById(R.id.suggestionRemoveImageView);

            SuggestionRemoveImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    suggestions.remove(SuggestionInputText.getText().toString());
                    suggestionArray.remove(SuggestionInputText.getText().toString());
                    notifyItemRemoved(position);
//                    notifyDataSetChanged();
                }
            });
        }
    }
}
