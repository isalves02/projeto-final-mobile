package com.example.projetofinalmobile;

import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;

public class PlaybackManager {
    private MediaPlayer mediaPlayer = null;
    private Recording currentRecording = null;
    private boolean isPlaying = false;

    private Handler handler = new Handler();
    private PlaybackListener listener;

    public interface PlaybackListener {
        void onPlay(Recording rec);
        void onPause();
        void onStop();
        void onCompletion();
        void onProgress(int progress);
    }

    public void setPlaybackListener(PlaybackListener listener) {
        this.listener = listener;
    }

    public Recording getCurrentRecording() {
        return currentRecording;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    // =============================
    // PLAY / PAUSE
    // =============================
    public void play(Recording recording) {
        if (mediaPlayer != null && currentRecording != null &&
                currentRecording.getId().equals(recording.getId()) &&
                isPlaying) {
            pause();
            return;
        }

        stop(); // vai pausar qualquer audio que esteja tocando

        currentRecording = recording;

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(recording.getFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;

            if (listener != null)
                listener.onPlay(recording);

            iniciarProgresso();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(mp -> {
            isPlaying = false;
            if (listener != null)
                listener.onCompletion();
            stop();
        });
    }

    public void pause() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;

            if (listener != null)
                listener.onPause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }

        mediaPlayer = null;
        isPlaying = false;

        if (listener != null)
            listener.onStop();
    }

    // =============================
    // PROGRESSO
    // =============================
    private void iniciarProgresso() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && isPlaying) {
                    int pos = mediaPlayer.getCurrentPosition();
                    int dur = mediaPlayer.getDuration();

                    int progress = (int) (((float) pos / dur) * 100);

                    if (listener != null)
                        listener.onProgress(progress);

                    handler.postDelayed(this, 200);
                }
            }
        });
    }
}