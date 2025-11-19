package com.example.projetofinalmobile;

public class Recording {
    private final String id;
    private String name;
    private final String filePath;
    private final long createdAt;
    private boolean favorite;

    public Recording(String id, String name, String filePath, long createdAt) {
        this(id, name, filePath, createdAt, false);
    }

    public Recording(String id, String name, String filePath, long createdAt, boolean favorite) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.createdAt = createdAt;
        this.favorite = favorite;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getFilePath() { return filePath; }
}
