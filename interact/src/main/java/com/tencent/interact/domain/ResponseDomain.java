package com.tencent.interact.domain;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/7/1 14:06
 */
public class ResponseDomain implements Serializable {
    private String total;
    private Object object;

    public ResponseDomain(String total, Object object) {
        this.total = total;
        this.object = object;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
