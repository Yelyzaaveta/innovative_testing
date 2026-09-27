import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/*
  @author   yelyza
  @project   Default (Template) Project
  @class  ${NAME}
  @version  1.0.0
  @since 10/09/2026
*/

// path to the book
static final String BOOK_FILE_PATH = "src/txt/harry.txt";

// how many most frequent words to print at the end
static final int TOP_WORDS_COUNT = 30;

void main() throws IOException {

    LocalDateTime start = LocalDateTime.now();

    String rawText = readBookText(BOOK_FILE_PATH);
    String cleanedText = cleanText(rawText);
    String[] sortedWords = splitAndSortWords(cleanedText);

    String[] uniqueWords = findUniqueWords(sortedWords);
    int[] wordFrequencies = countWordFrequencies(uniqueWords, sortedWords);

    String[] wordsWithFrequency = mergeWordsWithFrequencies(uniqueWords, wordFrequencies);
    sortByFrequencyAscending(wordsWithFrequency);

    printTopWords(wordsWithFrequency, TOP_WORDS_COUNT);

    LocalDateTime finish = LocalDateTime.now();
    System.out.println("Elapsed time (ms):");
    System.out.println(ChronoUnit.MILLIS.between(start, finish));
}

String readBookText(String filePath) throws IOException {
    return new String(Files.readAllBytes(Paths.get(filePath)));
}

String cleanText(String rawText) {
    return rawText.replaceAll("[^A-Za-z ]", " ").toLowerCase(Locale.ROOT);
}

String[] splitAndSortWords(String cleanedText) {
    String[] words = cleanedText.split(" +");
    Arrays.sort(words);
    return words;
}

String[] findUniqueWords(String[] sortedWords) {
    String[] uniqueWords = new String[sortedWords.length];
    int uniqueCount = 0;

    for (int i = 0; i < sortedWords.length; i++) {
        boolean alreadySeen = false;

        for (int j = 0; j < uniqueCount; j++) {
            if (uniqueWords[j].equals(sortedWords[i])) {
                alreadySeen = true;
                break;
            }
        }

        if (!alreadySeen) {
            uniqueWords[uniqueCount] = sortedWords[i];
            uniqueCount++;
        }
    }

    return Arrays.copyOf(uniqueWords, uniqueCount);
}

int[] countWordFrequencies(String[] uniqueWords, String[] allWords) {
    int[] frequencies = new int[uniqueWords.length];

    for (int i = 0; i < uniqueWords.length; i++) {
        int count = 0;
        for (int j = 0; j < allWords.length; j++) {
            if (uniqueWords[i].equals(allWords[j])) {
                count++;
            }
        }
        frequencies[i] = count;
    }

    return frequencies;
}

String[] mergeWordsWithFrequencies(String[] uniqueWords, int[] frequencies) {
    String[] wordsWithFrequency = new String[uniqueWords.length];

    for (int i = 0; i < uniqueWords.length; i++) {
        wordsWithFrequency[i] = uniqueWords[i] + " " + frequencies[i];
    }

    return wordsWithFrequency;
}

void sortByFrequencyAscending(String[] wordsWithFrequency) {
    Arrays.sort(wordsWithFrequency, Comparator.comparing(
            entry -> Integer.valueOf(entry.replaceAll("[^0-9]", ""))));
}

void printTopWords(String[] wordsWithFrequencySortedAscending, int topCount) {
    for (int i = 0; i < topCount; i++) {
        System.out.println(wordsWithFrequencySortedAscending[wordsWithFrequencySortedAscending.length - 1 - i]);
    }
}
