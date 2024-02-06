package com.masanz.ut2.ejercicio5.dao;

import com.masanz.ut2.ejercicio5.dto.ProductDTO;
import com.masanz.ut2.ejercicio5.dto.UserDTO;
import com.masanz.ut2.ejercicio5.init.DatabaseManager;

import java.util.LinkedHashMap;
import java.util.List;

public class UserDAO
{
    public static UserDTO insertIntoDatabase(LinkedHashMap<String, Object> mapArgs)
    {
        return (UserDTO) DatabaseManager.insertValues("INSERT INTO Users(full_name, user_, email, password_) VALUES(?, ?, ?, ?)","Users", mapArgs);
    }

    public static UserDTO getByID(LinkedHashMap<String, Object> mapArgs)
    {
        return (UserDTO) DatabaseManager.selectValues("SELECT * FROM Users_ WHERE id = ?", "Users", mapArgs).get(0);
    }

    @SuppressWarnings("unchecked")
    public static List<UserDTO> getAll()
    {
        return (List<UserDTO>) (Object) DatabaseManager.selectValues("SELECT * FROM Users_ ORDER BY id", "Users", new LinkedHashMap<>());
    }

    @SuppressWarnings("unchecked")
    public static List<UserDTO> authUserExists(LinkedHashMap<String, Object> mapArgs)
    {
        return (List<UserDTO>) (Object) DatabaseManager.selectValues("SELECT * FROM Users_ WHERE user_ = ? AND password_ = ?", "Users", mapArgs);
    }

    @SuppressWarnings("unchecked")
    public static List<ProductDTO> getProductsFromUser(LinkedHashMap<String, Object> mapArgs)
    {
        return (List<ProductDTO>) (Object) DatabaseManager.selectValues("SELECT * FROM Products WHERE userID = ?", "Products", mapArgs);
    }
}
