package com.example.projetofinalmobile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btnIniciarGravacao;
    private List<AudioItem> listaAudios = new ArrayList<>();
    private AudioAdapter audioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        RecyclerView recycler = findViewById(R.id.ContainerViewLista);

        audioAdapter = new AudioAdapter(listaAudios, new AudioAdapter.IOnAudioActions() {
            @Override
            public void onPlayPause(AudioItem item) {}

            @Override
            public void onDelete(AudioItem item) {}

            @Override
            public void onRename(AudioItem item) {}
        });


        // Configurando RecyclerView
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(audioAdapter);

        btnIniciarGravacao = findViewById(R.id.btnGravarAudio);

        btnIniciarGravacao.setOnClickListener(v -> {
            System.out.println("Teste -- audio" + v);
            // iniciarGravacao();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // teste
        adicionarNovoAudio("Gravação_001.mp3", 12);
    }

    private void adicionarNovoAudio(String nomeArquivo, int duracaoSegundos) {
        AudioItem novo = new AudioItem(nomeArquivo, duracaoSegundos);
        listaAudios.add(novo);
        audioAdapter.notifyItemInserted(listaAudios.size() - 1);
    }
}