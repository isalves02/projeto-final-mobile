package br.com.maddu.gravador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecordingManager {

    private final List<Recording> recordings = new ArrayList<>();

    // Criar gravação com nome (imputar nome)
    public Recording addRecording(String filePath, String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            displayName = "Gravação " + System.currentTimeMillis();
        }

        String id = UUID.randomUUID().toString();
        long createdAt = System.currentTimeMillis();

        Recording recording = new Recording(id, displayName, filePath, createdAt);
        recordings.add(recording);

        return recording;
    }

    // 2) Renomear gravação existente
    public boolean renameRecording(String id, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            return false;
        }

        for (Recording r : recordings) {
            if (r.getId().equals(id)) {
                r.setName(newName.trim());
                return true;
            }
        }
        return false;
    }

    // 3) Excluir gravação
    public boolean deleteRecording(String id) {
        Recording target = null;

        for (Recording r : recordings) {
            if (r.getId().equals(id)) {
                target = r;
                break;
            }
        }

        if (target == null) {
            return false;
        }

        File file = new File(target.getFilePath());
        boolean fileDeleted = true;

        if (file.exists()) {
            fileDeleted = file.delete();
        }

        recordings.remove(target);

        return fileDeleted;
    }

    // 4) Duplicar gravação
    // Cria uma nova com novo ID, nome "(cópia)" e mesma info de favorito
    public Recording duplicateRecording(String id) {
        for (Recording original : recordings) {
            if (original.getId().equals(id)) {

                String newId = UUID.randomUUID().toString();
                long createdAt = System.currentTimeMillis();

                String newName = original.getName() + " (cópia)";
                String newFilePath = original.getFilePath(); // aqui no "back" a gente só reaproveita

                Recording copy = new Recording(
                        newId,
                        newName,
                        newFilePath,
                        createdAt,
                        original.isFavorite()
                );

                recordings.add(copy);
                return copy;
            }
        }
        return null; // não achou
    }

    // 5) Favoritar / desfavoritar gravação (toggle)
    public boolean toggleFavorite(String id) {
        for (Recording r : recordings) {
            if (r.getId().equals(id)) {
                r.setFavorite(!r.isFavorite());
                return true;
            }
        }
        return false;
    }

    // listar todas
    public List<Recording> getAllRecordings() {
        return new ArrayList<>(recordings);
    }

    // listar só favoritas (se quiser usar depois)
    public List<Recording> getFavoriteRecordings() {
        List<Recording> favorites = new ArrayList<>();
        for (Recording r : recordings) {
            if (r.isFavorite()) {
                favorites.add(r);
            }
        }
        return favorites;
    }
}
