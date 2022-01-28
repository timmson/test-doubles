package ru.technicalExcellence.testDoubles;

import ru.technicalExcellence.testDoubles.model.Item;

import java.util.List;

public interface OrderService {

    void makeOrder(int clientId, List<Item> items) throws OrderException;
}
