package com.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class JsonResponse<T> {
    private Integer code;
    private String msg;
    private T data;
    private Map<String, Object> map = new HashMap<>();

    /**
     * 成功响应
     */
    public static <T> JsonResponse<T> success(T data) {
        JsonResponse<T> resp = new JsonResponse<>();
        resp.setData(data);
        resp.setCode(1);
        return resp;
    }

    /**
     * 失败响应
     */
    public static <T> JsonResponse<T> error(String msg) {
        JsonResponse<T> resp = new JsonResponse<>();
        resp.setMsg(msg);
        resp.setCode(0);
        return resp;
    }

    /**
     * 添加map数据
     */
    public JsonResponse<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
