package ru.otus.java.basic.http.server.app;

import ru.otus.java.basic.http.server.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsRepository {
    private List<Item> items;

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public ItemsRepository() {
        this.items = new ArrayList<>(Arrays.asList(
                new Item(1L, "Milk", BigDecimal.valueOf(80)),
                new Item(2L, "Bread", BigDecimal.valueOf(32)),
                new Item(3L, "Cheese", BigDecimal.valueOf(320))
        ));
    }

    public synchronized Item add(Item item) {
        Long newId = items.stream().mapToLong(Item::getId).max().orElse(0L) + 1L;
        item.setId(newId);
        items.add(item);
        return item;
    }

    public synchronized void delete(long id) {
        if (items.stream().noneMatch(i -> i.getId() == id)) {
            throw new NotFoundException(String.format("Элемент с идентификатором %d не найден.", id));
        }
        items = items.stream().filter(i -> i.getId() != id).collect(Collectors.toList());
    }

    public Item get(long id) {
        return items.stream().filter(i -> i.getId() == id).findFirst().orElseThrow(() ->
                new NotFoundException(String.format("Элемент с идентификатором %d не найден.", id)));
    }
}
