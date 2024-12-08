package onetomany.Users;

public enum Language {
    ENGLISH("en"),
    SPANISH("es");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Language fromCode(String code) {
        for (Language language : values()) {
            if (language.code.equalsIgnoreCase(code)) {
                return language;
            }
        }
        return ENGLISH; // Default to English if the code doesn't match
    }
}
