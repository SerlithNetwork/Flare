package co.technove.flare;

import org.jspecify.annotations.NonNull;

import java.net.URI;

public class FlareAuth {
    private final String token;
    private final URI uri;

    private FlareAuth(@NonNull String token, @NonNull URI uri) {
        this.token = token;
        this.uri = uri;
    }

    public static FlareAuth fromTokenAndUrl(@NonNull String token, @NonNull URI uri) {
        return new FlareAuth(token, uri);
    }

    public @NonNull String getToken() {
        return token;
    }

    public @NonNull URI getUri() {
        return uri;
    }
}
