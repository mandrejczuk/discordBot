package mandrejczuk.exceptions;

public class DiscordMessageException extends IllegalArgumentException{

    public DiscordMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiscordMessageException() {
    }

    public DiscordMessageException(String s) {
        super(s);
    }
}
