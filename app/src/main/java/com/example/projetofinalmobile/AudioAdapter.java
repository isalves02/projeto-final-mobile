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

    private List<AudioItem> listaAudios;
    private IOnAudioActions listener;

    public interface IOnAudioActions {
        void onPlayPause(AudioItem item);
        void onDelete(AudioItem item);
        void onRename(AudioItem item);
    }

    public AudioAdapter(List<AudioItem> listaAudios, IOnAudioActions listener) {
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
        AudioItem item = listaAudios.get(position);

        holder.txtNome.setText(item.getNomeArquivo());

        holder.btnPlayPause.setOnClickListener(v -> listener.onPlayPause(item));
        holder.btnExcluir.setOnClickListener(v -> listener.onDelete(item));
        holder.btnRenomear.setOnClickListener(v -> listener.onRename(item));
    }

    @Override
    public int getItemCount() {
        return listaAudios.size();
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder {

        TextView txtNome;
        ImageButton btnPlayPause, btnExcluir, btnRenomear;
        ProgressBar barraProgresso;

        public AudioViewHolder(View itemView) {
            super(itemView);

            txtNome = itemView.findViewById(R.id.txtNome);
            btnPlayPause = itemView.findViewById(R.id.btnPlayPause);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
            btnRenomear = itemView.findViewById(R.id.btnRenomear);
            barraProgresso = itemView.findViewById(R.id.progressoAudio);
        }
    }
}
