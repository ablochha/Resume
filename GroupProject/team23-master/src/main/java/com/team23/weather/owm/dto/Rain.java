
package com.team23.weather.owm.dto;

import java.util.HashMap;
import java.util.Map;

public class Rain {

    private Double _3h;
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The _3h
     */
    public Double get3h() {
        return _3h;
    }

    /**
     * 
     * @param _3h
     *     The 3h
     */
    public void set3h(Double _3h) {
        this._3h = _3h;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Rain{" +
                "_3h=" + _3h +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
