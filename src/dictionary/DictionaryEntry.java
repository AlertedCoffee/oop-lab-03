package dictionary;

import java.util.Locale;

final class DictionaryEntry {

    private final String source;
    private final String[] words;
    private final String translation;

    DictionaryEntry(String source, String translation) {
        this.source = source;
        this.translation = translation;
        String[] parts = source.split("\\s+");
        this.words = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            words[i] = parts[i].toLowerCase(Locale.ROOT);
        }
    }

    int getLeftLength() {
        return source.length();
    }

    String[] getWords() {
        return words;
    }

    String getTranslation() {
        return translation;
    }

    String getKey() {
        return String.join(" ", words);
    }
}
