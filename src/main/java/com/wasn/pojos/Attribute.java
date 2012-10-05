package com.wasn.pojos;

/**
 * To hold client and transaction attributes (key value pairs)
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class Attribute {

    String attributeName;
    String attributeValue;

    public Attribute(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

}
