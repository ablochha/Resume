
package com.team23.weather.owm.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResponse {

    private String message;
    private String cod;
    private Integer count;
    private java.util.List<SearchResponseItem> list = new ArrayList<SearchResponseItem>();
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     *     The cod
     */
    public String getCod() {
        return cod;
    }

    /**
     *
     * @param cod
     *     The cod
     */
    public void setCod(String cod) {
        this.cod = cod;
    }

    /**
     *
     * @return
     *     The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     *     The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     *     The list
     */
    public java.util.List<SearchResponseItem> getList() {
        return list;
    }

    /**
     *
     * @param listItem
     *     The list
     */
    public void setList(java.util.List<SearchResponseItem> listItem) {
        this.list = listItem;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    @Override
    public String toString() {
        return "SearchResponse{" +
                "message='" + message + '\'' +
                ", cod='" + cod + '\'' +
                ", count=" + count +
                ", list=" + list +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
