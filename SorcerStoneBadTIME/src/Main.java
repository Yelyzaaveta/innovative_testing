import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

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

    String[] sortedWords = readSortedWords(BOOK_FILE_PATH);

    String[] uniqueWords = findUniqueWords(sortedWords);
    int[] wordFrequencies = countWordFrequencies(uniqueWords, sortedWords);

    String[] topWords = findTopWords(uniqueWords, wordFrequencies, TOP_WORDS_COUNT);

    printTopWords(topWords);

    LocalDateTime finish = LocalDateTime.now();
    System.out.println("Elapsed time (ms):");
    System.out.println(ChronoUnit.MILLIS.between(start, finish));
}

// Optimization 1: the book is streamed through a small buffer instead of being loaded as one big String.
// Optimization 2: cleaning and splitting happen in one pass while reading, so no regex copy,
//                 no lowercase copy and no split() array.
String[] readSortedWords(String filePath) throws IOException {
    String[] words = new String[1024];
    int count = 0;
    StringBuilder word = new StringBuilder();

    try (InputStream in = new BufferedInputStream(Files.newInputStream(Paths.get(filePath)))) {
        int b;
        while ((b = in.read()) != -1) {
            if (b >= 'A' && b <= 'Z') {
                word.append((char) (b + ('a' - 'A')));
            } else if (b >= 'a' && b <= 'z') {
                word.append((char) b);
            } else if (word.length() > 0) {
                if (count == words.length) {
                    words = Arrays.copyOf(words, count * 2);
                }
                words[count++] = word.toString();
                word.setLength(0);
            }
        }
    }
    if (word.length() > 0) {
        if (count == words.length) {
            words = Arrays.copyOf(words, count + 1);
        }
        words[count++] = word.toString();
    }

    words = Arrays.copyOf(words, count);
    Arrays.sort(words);
    return words;
}

// Optimization 3: the array of unique words starts small and grows only when needed, instead of being
//                 allocated with the size of ALL words (there are far fewer unique words than words).
String[] findUniqueWords(String[] sortedWords) {
    String[] uniqueWords = new String[1024];
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
            if (uniqueCount == uniqueWords.length) {
                uniqueWords = Arrays.copyOf(uniqueWords, uniqueCount * 2);
            }
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

// Optimization 4: counts stay as ints, so no "word count" strings and no regex parsing inside the comparator.
// Optimization 5: only the top N are kept in a small heap instead of sorting every unique word;
//                 "word count" strings are built for those N only.
// Order is the same as before: higher frequency first, equal frequency -> alphabetically later word first.
String[] findTopWords(String[] words, int[] frequencies, int topCount) {
    Comparator<Integer> weakestFirst = Comparator
            .<Integer>comparingInt(i -> frequencies[i])
            .thenComparing(i -> words[i]);
    PriorityQueue<Integer> heap = new PriorityQueue<>(topCount + 1, weakestFirst);

    for (int i = 0; i < words.length; i++) {
        heap.offer(i);
        if (heap.size() > topCount) {
            heap.poll();
        }
    }

    String[] top = new String[heap.size()];
    for (int i = top.length - 1; i >= 0; i--) {
        int index = heap.poll();
        top[i] = words[index] + " " + frequencies[index];
    }
    return top;
}

void printTopWords(String[] topWords) {
    for (String line : topWords) {
        System.out.println(line);
    }
}
