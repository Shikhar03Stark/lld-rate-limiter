package com.shikhar03stark.server.cor;

import com.shikhar03stark.server.model.RequestContext;
import com.shikhar03stark.server.model.ResponseContext;

public abstract class Middleware {
    protected Middleware next;

    public void setNext(Middleware next) {
        this.next = next;
    }

    public Middleware getNext() {
        return next;
    }

    public ResponseContext handle(RequestContext requestContext) throws Exception {
        if (next != null) {
            return next.handle(requestContext);
        } else {
            return null;
        }
    }
}
