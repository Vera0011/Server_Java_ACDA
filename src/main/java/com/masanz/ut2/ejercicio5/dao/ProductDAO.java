package com.masanz.ut2.ejercicio5.dao;

import com.masanz.ut2.ejercicio5.dto.ProductDTO;
import com.masanz.ut2.ejercicio5.dto.UserDTO;
import com.masanz.ut2.ejercicio5.init.DatabaseManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ProductDAO
{
    public static ProductDTO insertIntoDatabase(LinkedHashMap<String, Object> mapArgs)
    {
        return (ProductDTO) DatabaseManager.insertValues("INSERT INTO Products(name_, value_, userID) VALUES(?, ?, ?)", "Products", mapArgs);
    }

    public static ProductDTO getByID(LinkedHashMap<String, Object> mapArgs)
    {
        return (ProductDTO) DatabaseManager.selectValues("SELECT * FROM Products WHERE id = ?", "Products", mapArgs).get(0);
    }

    @SuppressWarnings("unchecked")
    public static List<ProductDTO> getAll()
    {
        return (List<ProductDTO>) (Object) DatabaseManager.selectValues("SELECT * FROM Products ORDER BY id", "Products", new LinkedHashMap<>());
    }
}
