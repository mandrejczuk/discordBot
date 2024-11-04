package mandrejczuk.audio;

public enum Platform {
    YOUTUBE("ytsearch:"),SOUNDCLOUD("scsearch:");
    public final String search;
    Platform(String search) {
        this.search = search;
    }
}
