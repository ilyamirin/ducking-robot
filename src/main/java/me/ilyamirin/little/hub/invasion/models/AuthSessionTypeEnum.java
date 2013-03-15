package me.ilyamirin.little.hub.invasion.models;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "AuthSessionTypeEnum")
@XmlEnum
public enum AuthSessionTypeEnum {

    HUB;

    public String value() {
        return name();
    }

    public static AuthSessionTypeEnum fromValue(String v) {
        return valueOf(v);
    }
}
