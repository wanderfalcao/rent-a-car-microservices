package br.com.infnet.wander.utility;

import org.springframework.stereotype.Component;

@Component
public class TransactionIdentifier {

    private final ThreadLocal<String> id = new ThreadLocal<>();

    public String getTransactionId() {
        return id.get();
    }

    public void setTransactionId(String value) {
        id.set(value);
    }

    public void unload(){
        id.remove();
    }
}
