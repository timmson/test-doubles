package ru.technicalExcellence.testDoubles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ShoppingService {

    private CartDAO cartDAO;

    private ItemDAO itemDAO;

    private OrderService orderService;

    public ShoppingService(CartDAO cartDAO, ItemDAO itemDAO, OrderService orderService) {
        this.cartDAO = cartDAO;
        this.itemDAO = itemDAO;
        this.orderService = orderService;
    }

    public void getCartItems(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ShoppingServiceException {
        final var clientId = getClientId(httpServletRequest);
        final var items = cartDAO.findCartItemsByClientId(clientId);

        try {
            sendResponse(httpServletResponse, items.toString());
        } catch (IOException e) {
            sendError(httpServletResponse);
            throw new ShoppingServiceException("Internal error", e);
        }
    }

    public void putItemInCart(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ShoppingServiceException {
        final var clientId = getClientId(httpServletRequest);
        final var parameters = httpServletRequest.getParameterMap();

        try {
            final var item = itemDAO.findById(Integer.parseInt(parameters.get("id")[0], 10));
            final var quantity = Integer.parseInt(parameters.get("quantity")[0], 10);

            cartDAO.addItemToCard(clientId, item, quantity);
            sendOk(httpServletResponse);
        } catch (SQLException | IOException | ItemException e) {
            sendError(httpServletResponse);
            throw new ShoppingServiceException("Internal error while removing item", e);
        }
    }

    public void removeItemFromCart(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ShoppingServiceException {
        final var clientId = getClientId(httpServletRequest);
        final var parameters = httpServletRequest.getParameterMap();

        try {
            final var item = itemDAO.findById(Integer.parseInt(parameters.get("id")[0], 10));
            final var quantity = Integer.parseInt(parameters.get("quantity")[0], 10);

            cartDAO.removeItemToCard(clientId, item, quantity);

            sendOk(httpServletResponse);
        } catch (SQLException | IOException | ItemException e) {
            sendError(httpServletResponse);
            throw new ShoppingServiceException("Internal error while removing item", e);
        }
    }

    public void makeOrder(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ShoppingServiceException {
        final var clientId = getClientId(httpServletRequest);
        final var items = cartDAO.findCartItemsByClientId(clientId);
        try {
            orderService.makeOrder(clientId, items);
            sendResponse(httpServletResponse, "OK");
        } catch (OrderException | IOException e) {
            sendError(httpServletResponse);
            throw new ShoppingServiceException("Internal error while make order", e);
        }
    }

    private int getClientId(HttpServletRequest httpServletRequest) throws ShoppingServiceException {
        final var session = httpServletRequest.getSession();
        if (session == null) {
            throw new ShoppingServiceException("Client is not authenticated");
        }
        return Integer.parseInt(session.getAttribute("id").toString(), 10);
    }

    private void sendOk(HttpServletResponse httpServletResponse) throws IOException {
        sendResponse(httpServletResponse, "OK");
    }

    private void sendError(HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse.sendError(500, "Internal error");
        } catch (IOException ignore) {
        }
    }

    private void sendResponse(HttpServletResponse httpServletResponse, String body) throws IOException {
        httpServletResponse.getWriter().println(body);
    }

}
