package com.olalekan.naijakeyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olalekan.naijakeyboard.defaults.Constants;

import java.util.ArrayList;

public class ColourRecyclerAdapter extends RecyclerView.Adapter<ColourRecyclerAdapter.ColourViewHolder> {

    Context mContext;
    LayoutInflater mLayoutInflater;
    int colour = 0;

    ArrayList<Integer> colourArray = Constants.COLOUR_ARRAY;

    public ColourRecyclerAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ColourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.single_colour_view, parent, false);
        return new ColourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColourViewHolder holder, int position) {
        holder.mColourView.setColour(colourArray.get(position));
    }

    @Override
    public int getItemCount() {
        return colourArray.size();
    }

    public int getColour(){
        return colour;
    }

    class ColourViewHolder extends RecyclerView.ViewHolder{
        private final ColourView mColourView;

        public ColourViewHolder(@NonNull View itemView) {
            super(itemView);
            mColourView = itemView.findViewById(R.id.singleColourView);

            mColourView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mColourView.setChecked();
                    colour = colourArray.get(getAdapterPosition());
//                    mColourView.setColour(0xf0ffff00);
                }
            });
        }
    }
}
