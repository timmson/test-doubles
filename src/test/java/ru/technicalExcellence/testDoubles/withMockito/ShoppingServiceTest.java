package ru.technicalExcellence.testDoubles.withMockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.technicalExcellence.testDoubles.CartDAO;
import ru.technicalExcellence.testDoubles.ShoppingService;
import ru.technicalExcellence.testDoubles.ShoppingServiceException;
import ru.technicalExcellence.testDoubles.model.Item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceTest {

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
    void setUp() throws IOException {
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        shoppingService = new ShoppingService(cartDAO, null, null);
    }

    @Test
    void getCartItems() throws ShoppingServiceException {
        final var item = new Item();
        item.setId(1);
        final var items = List.of(item);

        when(httpSession.getAttribute("id")).thenReturn(1);
        when(cartDAO.findCartItemsByClientId(1)).thenReturn(items);

        shoppingService.getCartItems(httpServletRequest, httpServletResponse);

        verify(printWriter, times(1)).println(items.toString());
    }
}