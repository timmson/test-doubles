package ru.technicalExcellence.testDoubles;

import ru.technicalExcellence.testDoubles.model.Item;

import java.sql.SQLException;
import java.util.List;

public interface CartDAO {

    List<Item> findCartItemsByClientId(Integer id) throws SQLException;

    void removeItemToCard(int clientId, Item item, int quantity) throws SQLException;

    void addItemToCard(int clientId, Item item, int quantity) throws SQLException;
}
