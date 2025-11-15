package br.com.maddu.gravador;

public class Recording {
    private final String id;       // identificador
    private String name;           // nome q o usuário vê
    private final String filePath; // caminho do arquivo de audio
    private final long createdAt;  // timestamp
    private boolean favorite;      // se é favorita ou não

    public Recording(String id, String name, String filePath, long createdAt) {
        this(id, name, filePath, createdAt, false);
    }

    // construtor completo (usado na duplicação)
    public Recording(String id, String name, String filePath, long createdAt, boolean favorite) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.createdAt = createdAt;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Recording{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", createdAt=" + createdAt +
                ", favorite=" + favorite +
                '}';
    }
}
