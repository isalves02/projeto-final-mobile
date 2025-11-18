package com.example.projetofinalmobile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION = 1001;

    // Componentes da UI
    private FloatingActionButton btnIniciarGravacao;
    private TextView txtCronometro;

    // Gravação de áudio
    private boolean isRecording = false;
    private MediaRecorder recorder;
    private String currentFilePath;

    // Lista de gravações
    private List<Recording> listaGravacoes = new ArrayList<>();
    private AudioAdapter audioAdapter;

    // Cronômetro
    private long startTime = 0;
    private Handler handler = new Handler();
    private Runnable cronometroRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        configurarRecyclerView();
        configurarBotaoGravacao();
        aplicarWindowInsets();
    }

    // ============================================================
    // INICIALIZAÇÃO
    // ============================================================

    private void inicializarComponentes() {
        txtCronometro = findViewById(R.id.cronometro);
        btnIniciarGravacao = findViewById(R.id.btnGravarAudio);
    }

    private void configurarRecyclerView() {
        RecyclerView recycler = findViewById(R.id.ContainerViewLista);
        audioAdapter = new AudioAdapter(listaGravacoes, new AudioAdapter.IOnAudioActions() {
            @Override
            public void onPlayPause(Recording item) {
                Toast.makeText(MainActivity.this, "Play/Pause: " + item.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(Recording item) {
                deletarGravacao(item);
            }

            @Override
            public void onRename(Recording item) {
                Toast.makeText(MainActivity.this, "Renomear: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(audioAdapter);
    }

    private void configurarBotaoGravacao() {
        btnIniciarGravacao.setOnClickListener(v -> {
            if (!isRecording) {
                verificarPermissaoEIniciar();
            } else {
                pararGravacao();
            }
        });
    }

    private void aplicarWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // ============================================================
    // GERENCIAMENTO DE PERMISSÕES
    // ============================================================

    private void verificarPermissaoEIniciar() {
        if (temPermissaoAudio()) {
            iniciarGravacao();
        } else {
            solicitarPermissaoAudio();
        }
    }

    private boolean temPermissaoAudio() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void solicitarPermissaoAudio() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_AUDIO_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_AUDIO_PERMISSION) {
            if (permissaoConcedida(grantResults)) {
                iniciarGravacao();
            } else {
                mostrarToast("Permissão de microfone negada!");
            }
        }
    }

    private boolean permissaoConcedida(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    // ============================================================
    // CONTROLE DE GRAVAÇÃO
    // ============================================================

    private void iniciarGravacao() {
        try {
            prepararGravacao();
            recorder.start();

            isRecording = true;
            atualizarUIGravacao(true);
            iniciarCronometro();
            mostrarToast("Gravando...");

        } catch (IOException e) {
            e.printStackTrace();
            mostrarToast("Erro ao iniciar gravação!");
            liberarRecorder();
        }
    }

    private void prepararGravacao() throws IOException {
        currentFilePath = gerarCaminhoArquivo();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(currentFilePath);
        recorder.prepare();
    }

    private String gerarCaminhoArquivo() {
        return getExternalFilesDir(null).getAbsolutePath()
                + "/REC_" + System.currentTimeMillis() + ".mp4";
    }

    private void pararGravacao() {
        try {
            if (recorder != null) {
                recorder.stop();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            liberarRecorder();
        }

        isRecording = false;
        atualizarUIGravacao(false);
        pararCronometro();
        salvarGravacao();
        mostrarToast("Gravação salva!");
    }

    private void liberarRecorder() {
        if (recorder != null) {
            try {
                recorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recorder = null;
            }
        }
    }

    private void salvarGravacao() {
        Recording nova = new Recording(
                UUID.randomUUID().toString(),
                "Gravação " + (listaGravacoes.size() + 1),
                currentFilePath,
                System.currentTimeMillis()
        );

        listaGravacoes.add(nova);
        audioAdapter.notifyItemInserted(listaGravacoes.size() - 1);
    }

    private void deletarGravacao(Recording item) {
        int position = listaGravacoes.indexOf(item);
        if (position != -1) {
            listaGravacoes.remove(position);
            audioAdapter.notifyItemRemoved(position);
        }
    }

    private void atualizarUIGravacao(boolean gravando) {
        int icone = gravando
                ? android.R.drawable.ic_media_pause
                : android.R.drawable.ic_media_play;
        btnIniciarGravacao.setImageResource(icone);
    }

    // ============================================================
    // CRONÔMETRO
    // ============================================================

    private void iniciarCronometro() {
        startTime = System.currentTimeMillis();
        cronometroRunnable = new Runnable() {
            @Override
            public void run() {
                atualizarDisplayCronometro();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(cronometroRunnable);
    }

    private void atualizarDisplayCronometro() {
        long tempo = System.currentTimeMillis() - startTime;
        int segundos = (int) (tempo / 1000);
        int minutos = segundos / 60;
        segundos = segundos % 60;

        txtCronometro.setText(String.format("%02d:%02d", minutos, segundos));
    }

    private void pararCronometro() {
        if (cronometroRunnable != null) {
            handler.removeCallbacks(cronometroRunnable);
        }
        txtCronometro.setText("00:00");
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    private void mostrarToast(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pararCronometro();
        liberarRecorder();
    }
}