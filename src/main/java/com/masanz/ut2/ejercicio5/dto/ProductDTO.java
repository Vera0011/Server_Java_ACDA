package com.masanz.ut2.ejercicio5.dto;

import com.masanz.ut2.ejercicio5.dao.ProductDAO;

import java.util.LinkedHashMap;
import java.util.Objects;

public class ProductDTO {
    private int id;
    private String name_;
    private double value_;
    private int userID;

    public ProductDTO(int id, String name_, double value_, int userID) {
        this.id = id;
        this.name_ = name_;
        this.value_ = value_;
        this.userID = userID;
    }

    /* Manual creation */
    public ProductDTO(String name_, double value_) {
        this.name_ = name_;
        this.value_ = value_;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public double getValue_() {
        return value_;
    }

    public void setValue_(double value_) {
        this.value_ = value_;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public ProductDTO getProductByID(int id) {
        LinkedHashMap<String, Object> columnNames = new LinkedHashMap<>();
        columnNames.put("id", id);

        return ProductDAO.getByID(columnNames);
    }

    public ProductDTO insertIntoDatabase(int userID) {
        LinkedHashMap<String, Object> columnNames = new LinkedHashMap<>();

        columnNames.put("name_", this.name_);
        columnNames.put("value_", this.value_);
        columnNames.put("userID", userID);

        return ProductDAO.insertIntoDatabase(columnNames);
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name_='" + name_ + '\'' +
                ", value_=" + value_ +
                ", userID=" + userID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return id == that.id && Double.compare(value_, that.value_) == 0 && userID == that.userID && Objects.equals(name_, that.name_);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name_, value_, userID);
    }
}
