package com.example.projetofinalmobile;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private LinearLayout containerListaAudios;
    private FloatingActionButton btnIniciarGravacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        containerListaAudios = findViewById(R.id.containerAudios);
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

        // adicionarNovoAudio("Gravação_001.mp3", 12);
    }
    private void adicionarNovoAudio(String nomeArquivo, int duracaoSegundos) {
        TextView novoItem = new TextView(this);
        novoItem.setText("🎙 " + nomeArquivo + " (" + duracaoSegundos + "s)");
        novoItem.setTextSize(18);
        novoItem.setPadding(16, 16, 16, 16);

        containerListaAudios.addView(novoItem);
    }
}
