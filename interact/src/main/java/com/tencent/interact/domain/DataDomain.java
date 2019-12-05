package com.tencent.interact.domain;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author healen
 * @Date 2019/7/1 14:06
 */
public class DataDomain implements Serializable {
    private String type;
    private Object object;

    public DataDomain() {
        this.type = type;
        this.object = object;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
