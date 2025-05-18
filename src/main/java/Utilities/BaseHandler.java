package Utilities;

import Classes.RequestHandler;

import java.util.Map;

public class BaseHandler implements RequestHandler {
    protected RequestHandler nextHandler;

    public boolean handle(Map<String, Object> selectedRow, double requestedAmount){
        // Default Value
        return true;
    }

    @Override
    public void setNextHandler(RequestHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected boolean processNext(Map<String, Object> selectedRow, double requestedAmount) {
        return nextHandler != null ? nextHandler.handle(selectedRow, requestedAmount) : true;
    }
}
