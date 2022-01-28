package ru.technicalExcellence.testDoubles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.technicalExcellence.testDoubles.model.Item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceWithMockitoTest {

    private ShoppingService shoppingService;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpSession httpSession;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        when(httpServletRequest.getSession()).thenReturn(httpSession);

        shoppingService = new ShoppingService(cartDAO, null, null);
    }

    @Test
    void getCartItems() throws ShoppingServiceException, SQLException, IOException {
        final var item = new Item();
        item.setId(1);
        final var items = List.of(item);

        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(httpSession.getAttribute("id")).thenReturn(1);
        when(cartDAO.findCartItemsByClientId(1)).thenReturn(items);

        shoppingService.getCartItems(httpServletRequest, httpServletResponse);

        verify(printWriter, times(1)).println(items.toString());
    }

    @Test
    void getCartItemsWhenCardDAOThrowsException() throws IOException, SQLException {
        when(httpSession.getAttribute("id")).thenReturn(1);
        when(cartDAO.findCartItemsByClientId(1)).thenThrow(new SQLException("table was not found"));

        assertThrows(ShoppingServiceException.class, () -> shoppingService.getCartItems(httpServletRequest, httpServletResponse));

        verify(httpServletResponse, times(1)).sendError(eq(500), eq("Internal error"));
    }
}