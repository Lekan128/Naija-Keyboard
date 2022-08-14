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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmojiOptionsRecyclerAdapter extends RecyclerView.Adapter<EmojiOptionsRecyclerAdapter.EmojiOptionsRecyclerviewViewHolder>{

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> mOptions;

    com.olalekan.naijakeyboard.EmojiRecyclerAdapter.EmojiWasClicked mEmojiWasClicked = com.olalekan.naijakeyboard.MyInputMethod.getInstance();

    public EmojiOptionsRecyclerAdapter(Context context, ArrayList<String> options) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mOptions = options;
    }

    @NonNull
    @Override
    public EmojiOptionsRecyclerviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.single_emoji, parent,false);
        return new EmojiOptionsRecyclerviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiOptionsRecyclerviewViewHolder holder, int position) {
        holder.previewText.setText(mOptions.get(position));
    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    class EmojiOptionsRecyclerviewViewHolder extends RecyclerView.ViewHolder {
        final TextView previewText;
        final FrameLayout keyFrame;

        public EmojiOptionsRecyclerviewViewHolder(@NonNull View itemView) {
            super(itemView);
            previewText = itemView.findViewById(R.id.emojiTextView);
            keyFrame = itemView.findViewById(R.id.emojiFrame);

            keyFrame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));


            previewText.setPadding(PREVIEW_PADDING_LEFT_RIGHT, PREVIEW_PADDING_TOP_BUTTOM,
                    PREVIEW_PADDING_LEFT_RIGHT,PREVIEW_PADDING_TOP_BUTTOM);
            previewText.setTextSize(PREVIEW_TEXT_SIZE);


            previewText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    KeyView keyView = new KeyView(view.getContext());
//                    keyView.setDefaultValueForDummyPreviewKey(previewText.getText().toString());
//                    mKeyView.getKeyWasClicked().onClick(keyView);
//                    mKeyView.dismissOptionsPopup();
                    mEmojiWasClicked.emojiClicked(previewText.getText().toString());

                }
            });
        }
    }
}
