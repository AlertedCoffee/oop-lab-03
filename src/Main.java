import dictionary.Dictionary;
import dictionary.DictionaryReader;
import exception.FileReadException;
import exception.InvalidFileFormatException;
import translator.Translator;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String path = args.length > 0 ? args[0].trim() : readLine(scanner, "Файл словаря: ");
            Dictionary dictionary = new DictionaryReader().read(toPath(path));
            Translator translator = new Translator(dictionary);
            System.out.println("Словарь загружен (" + dictionary.size() + ").");
            System.out.println("Введите текст для перевода. Пустая строка — выход.");
            while (true) {
                String text = readLine(scanner, "> ");
                if (text.isEmpty()) {
                    break;
                }
                System.out.println(translator.translate(text));
            }
        } catch (FileReadException | InvalidFileFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    private static Path toPath(String path) throws FileReadException {
        if (path.isBlank()) {
            throw new FileReadException("Путь к файлу словаря не задан.");
        }
        try {
            return Path.of(path);
        } catch (InvalidPathException e) {
            throw new FileReadException("Некорректный путь к файлу: " + path, e);
        }
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "";
        }
        return scanner.nextLine();
    }
}
