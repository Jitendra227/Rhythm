package com.jitendra.rhythm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jitendra.rhythm.model.AudioItems;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.viewHolder> {
    Context context;
    ArrayList<AudioItems> items;
    public OnItemClickListener onItemClickListener;

    public AudioAdapter(Context context,ArrayList<AudioItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public AudioAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioAdapter.viewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }




    public class viewHolder extends RecyclerView.ViewHolder{
        private TextView title, artist;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.audio_title);
            artist = itemView.findViewById(R.id.audio_artist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(),v);
                }
            });
        }

        public void bind(AudioItems audioItems) {
            title.setText(audioItems.getAudioTitle());
            artist.setText(audioItems.getAudioArtist());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }


}