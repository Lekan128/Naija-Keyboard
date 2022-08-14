package com.olalekan.naijakeyboard;

import static com.olalekan.naijakeyboard.KeyView.PREVIEW_PADDING_LEFT_RIGHT;
import static com.olalekan.naijakeyboard.KeyView.PREVIEW_PADDING_TOP_BUTTOM;
import static com.olalekan.naijakeyboard.KeyView.PREVIEW_TEXT_SIZE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PreviewRecyclerAdapter extends RecyclerView.Adapter<PreviewRecyclerAdapter.PreviewRecyclerviewViewHolder>{

    KeyView mKeyView;
    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> mLetters;

    public PreviewRecyclerAdapter(KeyView keyView) {
        mKeyView = keyView;
        mContext = keyView.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mLetters = keyView.getOptions();
    }

    @NonNull
    @Override
    public PreviewRecyclerviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.key_view, parent,false);
        return new PreviewRecyclerviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewRecyclerviewViewHolder holder, int position) {
        holder.previewText.setText(mLetters.get(position));
    }

    @Override
    public int getItemCount() {
        return mLetters.size();
    }

    class PreviewRecyclerviewViewHolder extends RecyclerView.ViewHolder {
        final TextView previewText;
        final CardView previewCardView;
        final FrameLayout keyFrame;

        public PreviewRecyclerviewViewHolder(@NonNull View itemView) {
            super(itemView);
            previewText = itemView.findViewById(R.id.letterTextView);
            previewCardView = itemView.findViewById(R.id.letterCard);
            keyFrame = itemView.findViewById(R.id.keyFrame);

            keyFrame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));


            previewText.setPadding(PREVIEW_PADDING_LEFT_RIGHT, PREVIEW_PADDING_TOP_BUTTOM,
                    PREVIEW_PADDING_LEFT_RIGHT,PREVIEW_PADDING_TOP_BUTTOM);
            previewText.setTextSize(PREVIEW_TEXT_SIZE);

            previewCardView.setCardBackgroundColor(0xffffffff);
            previewCardView.setCardElevation(0);

            previewCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KeyView keyView = new KeyView(view.getContext());
                    keyView.setDefaultValueForDummyPreviewKey(previewText.getText().toString());
                    mKeyView.getKeyWasClicked().onClick(keyView);
                    mKeyView.dismissOptionsPopup();
                }
            });
        }
    }
}
