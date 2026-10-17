package chnu.edu.APILAB.repository;


/*
 @author  Orynchuk Yelyzaveta
  @project   APILAB
  @class  ItemRepository
  @version  1.0.0 
  @since 27/09/2026 - 19.04
*/

import chnu.edu.APILAB.model.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// Items are kept in a text file instead of a database. One line = one item:
// id <TAB> name <TAB> code <TAB> quantity   (name and code are URL-encoded, so they can hold any symbols).
// The file is read once on start and rewritten after every change.
@Repository
public class ItemRepository {

    private final Path file;
    private final Map<String, Item> items = new LinkedHashMap<>();

    public ItemRepository(@Value("${items.file:data/items.tsv}") String filePath) {
        this.file = Path.of(filePath);
        load();
    }

    public synchronized List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    public synchronized Optional<Item> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    public synchronized Optional<Item> findByCode(String code) {
        return items.values().stream().filter(item -> item.getCode().equals(code)).findFirst();
    }

    public synchronized boolean existsById(String id) {
        return items.containsKey(id);
    }

    public synchronized long count() {
        return items.size();
    }

    // a new item (no id yet) gets a generated id, an item with a known id replaces the old one
    public synchronized Item save(Item item) {
        if (item.getId() == null) {
            item.setId(UUID.randomUUID().toString());
        }
        items.put(item.getId(), item);
        store();
        return item;
    }

    public synchronized void saveAll(List<Item> newItems) {
        for (Item item : newItems) {
            if (item.getId() == null) {
                item.setId(UUID.randomUUID().toString());
            }
            items.put(item.getId(), item);
        }
        store();
    }

    public synchronized void deleteById(String id) {
        if (items.remove(id) != null) {
            store();
        }
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\t", -1);
                Item item = new Item(decode(parts[1]), decode(parts[2]), Integer.parseInt(parts[3]));
                item.setId(parts[0]);
                items.put(item.getId(), item);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read items file " + file, e);
        }
    }

    private void store() {
        List<String> lines = new ArrayList<>();
        for (Item item : items.values()) {
            lines.add(item.getId() + "\t" + encode(item.getName()) + "\t" + encode(item.getCode())
                    + "\t" + item.getQuantity());
        }
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write items file " + file, e);
        }
    }

    private static String encode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }

    private static String decode(String text) {
        return URLDecoder.decode(text, StandardCharsets.UTF_8);
    }
}
