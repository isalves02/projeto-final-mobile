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
    private Recording recordingTocando = null;
    private boolean isPlaying = false;
    private int progresso = 0;

    public interface IOnAudioActions {
        void onPlayPause(Recording item);
        void onDelete(Recording item);
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

        boolean thisIsPlaying = (recordingTocando != null &&
                recordingTocando.getId().equals(rec.getId()));

        if (thisIsPlaying && isPlaying) {
            holder.btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            holder.barraProgresso.setProgress(progresso);
        } else {
            holder.btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            holder.barraProgresso.setProgress(0);
        }

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

    public void atualizarProgressoAudio(Recording rec, boolean isPlaying, int progressoAtual) {
        this.recordingTocando = rec;
        this.isPlaying = isPlaying;
        this.progresso = progressoAtual;
        notifyDataSetChanged();
    }
}
