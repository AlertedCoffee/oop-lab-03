package translator;

import dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

public final class Translator {

    private final Dictionary dictionary;

    public Translator(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String translate(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        List<Token> tokens = tokenize(text);
        StringBuilder result = new StringBuilder(text.length());
        int index = 0;
        while (index < tokens.size()) {
            Token token = tokens.get(index);
            if (!token.isWord()) {
                result.append(token.getText());
                index++;
                continue;
            }
            index = appendTranslated(tokens, index, result);
        }
        return result.toString();
    }

    private int appendTranslated(List<Token> tokens, int index, StringBuilder result) {
        List<String> words = new ArrayList<>();
        List<Integer> wordTokenIndexes = new ArrayList<>();
        int cursor = index;
        while (cursor < tokens.size() && tokens.get(cursor).isWord()) {
            words.add(tokens.get(cursor).getText());
            wordTokenIndexes.add(cursor);
            int separator = cursor + 1;
            if (separator >= tokens.size() || !tokens.get(separator).isWhitespace()) {
                break;
            }
            cursor = separator + 1;
        }

        Dictionary.Match match = dictionary.findLongest(words);
        if (match == null) {
            result.append(tokens.get(index).getText());
            return index + 1;
        }
        result.append(match.getTranslation());
        return wordTokenIndexes.get(match.getWordCount() - 1) + 1;
    }

    private List<Token> tokenize(String text) {
        List<Token> tokens = new ArrayList<>();
        int index = 0;
        while (index < text.length()) {
            int start = index;
            boolean word = Character.isLetter(text.charAt(index));
            if (word) {
                index++;
                while (index < text.length() && Character.isLetter(text.charAt(index))) {
                    index++;
                }
            } else {
                index++;
                while (index < text.length() && !Character.isLetter(text.charAt(index))) {
                    index++;
                }
            }
            String value = text.substring(start, index);
            tokens.add(new Token(value, word, word ? false : value.isBlank()));
        }
        return tokens;
    }

    private static final class Token {

        private final String text;
        private final boolean word;
        private final boolean whitespace;

        private Token(String text, boolean word, boolean whitespace) {
            this.text = text;
            this.word = word;
            this.whitespace = whitespace;
        }

        private String getText() {
            return text;
        }

        private boolean isWord() {
            return word;
        }

        private boolean isWhitespace() {
            return whitespace;
        }
    }
}
