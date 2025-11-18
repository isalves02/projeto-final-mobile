package com.example.projetofinalmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {
    public interface IOnAudioActions {
        void onPlayPause(Recording item);
        void onDelete(Recording item);
        void onRename(Recording item);
    }

    private List<Recording> listaAudios;
    private IOnAudioActions listener;

    public AudioAdapter(List<Recording> listaAudios, IOnAudioActions listener) {
        this.listaAudios = listaAudios;
        this.listener = listener;
    }

    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_item, parent, false);
        return new AudioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        Recording rec = listaAudios.get(position);
        holder.txtNome.setText(rec.getName());

        holder.btnPlayPause.setOnClickListener(v -> listener.onPlayPause(rec));
        holder.btnExcluir.setOnClickListener(v -> listener.onDelete(rec));
    }

    @Override
    public int getItemCount() {
        return listaAudios.size();
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;
        ImageButton btnPlayPause, btnExcluir;
        ProgressBar barraProgresso;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNome = itemView.findViewById(R.id.txtNome);
            btnPlayPause = itemView.findViewById(R.id.btnPlayPause);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
            barraProgresso = itemView.findViewById(R.id.progressoAudio);
        }
    }
}
