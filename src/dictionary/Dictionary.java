package dictionary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class Dictionary {

    private final List<DictionaryEntry> entries;

    Dictionary(List<DictionaryEntry> loaded) {
        Map<String, DictionaryEntry> unique = new LinkedHashMap<>();
        for (DictionaryEntry entry : loaded) {
            unique.put(entry.getKey(), entry);
        }
        List<DictionaryEntry> sorted = new ArrayList<>(unique.values());
        sorted.sort(Comparator.comparingInt(DictionaryEntry::getLeftLength).reversed());
        this.entries = List.copyOf(sorted);
    }

    public int size() {
        return entries.size();
    }

    public Match findLongest(List<String> words) {
        for (DictionaryEntry entry : entries) {
            String[] phrase = entry.getWords();
            if (phrase.length > words.size()) {
                continue;
            }
            if (matches(words, phrase)) {
                return new Match(entry.getTranslation(), phrase.length);
            }
        }
        return null;
    }

    private boolean matches(List<String> words, String[] phrase) {
        for (int i = 0; i < phrase.length; i++) {
            if (!words.get(i).toLowerCase(Locale.ROOT).equals(phrase[i])) {
                return false;
            }
        }
        return true;
    }

    public static final class Match {

        private final String translation;
        private final int wordCount;

        private Match(String translation, int wordCount) {
            this.translation = translation;
            this.wordCount = wordCount;
        }

        public String getTranslation() {
            return translation;
        }

        public int getWordCount() {
            return wordCount;
        }
    }
}
