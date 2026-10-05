package dictionary;

import exception.FileReadException;
import exception.InvalidFileFormatException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class DictionaryReader {

    public Dictionary read(Path path) throws FileReadException, InvalidFileFormatException {
        if (path == null) {
            throw new FileReadException("Путь к файлу словаря не задан.");
        }
        if (!Files.exists(path)) {
            throw new FileReadException("Файл не существует: " + path);
        }
        if (Files.isDirectory(path)) {
            throw new FileReadException("Ожидался файл, но указан каталог: " + path);
        }
        if (!Files.isReadable(path)) {
            throw new FileReadException("Нет доступа к файлу: " + path);
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileReadException("Не удалось прочитать файл: " + path, e);
        }

        if (!lines.isEmpty()) {
            lines.set(0, stripBom(lines.get(0)));
        }

        List<DictionaryEntry> entries = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank() || line.startsWith(";")) {
                continue;
            }
            entries.add(parseLine(line, i + 1));
        }
        return new Dictionary(entries);
    }

    private DictionaryEntry parseLine(String line, int lineNumber) throws InvalidFileFormatException {
        int separator = line.indexOf('|');
        if (separator < 0 || line.indexOf('|', separator + 1) >= 0) {
            throw new InvalidFileFormatException(lineNumber, line);
        }
        String source = line.substring(0, separator).trim();
        String translation = line.substring(separator + 1).trim();
        if (source.isEmpty() || translation.isEmpty()) {
            throw new InvalidFileFormatException(lineNumber, line);
        }
        return new DictionaryEntry(source, translation);
    }

    private String stripBom(String line) {
        if (!line.isEmpty() && line.charAt(0) == '\uFEFF') {
            return line.substring(1);
        }
        return line;
    }
}
