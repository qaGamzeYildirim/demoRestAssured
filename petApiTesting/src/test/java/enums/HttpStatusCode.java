package enums;

public enum HttpStatusCode {
    OK(200, "OK"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed");

    private final int code;
    private final String reasonPhrase;

    HttpStatusCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int getCode() {
        return code;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public static HttpStatusCode fromCode(int code) {
        for (HttpStatusCode status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown HTTP Status Code: " + code);
    }

    @Override
    public String toString() {
        return code + " " + reasonPhrase;
    }
}

