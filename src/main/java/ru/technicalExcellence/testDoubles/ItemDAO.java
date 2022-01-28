package ru.technicalExcellence.testDoubles;

import ru.technicalExcellence.testDoubles.model.Item;

public interface ItemDAO {
    Item findById(int id) throws ItemException;
}
