package Classes;

import java.util.Map;

public interface RequestHandler {
    boolean handle(Map<String, Object> selectedRow, double requestedAmount);
    void setNextHandler(RequestHandler nextHandler);
}
