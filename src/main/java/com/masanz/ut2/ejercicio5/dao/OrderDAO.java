package com.masanz.ut2.ejercicio5.dao;

import com.masanz.ut2.ejercicio5.dto.OrderDTO;
import com.masanz.ut2.ejercicio5.dto.UserDTO;
import com.masanz.ut2.ejercicio5.init.DatabaseManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderDAO
{
    public static OrderDTO getByID(LinkedHashMap<String, Object> mapArgs)
    {
        return (OrderDTO) DatabaseManager.selectValues("SELECT * FROM Orders WHERE id = ?", "Orders", mapArgs).get(0);
    }

    @SuppressWarnings("unchecked")
    public static List<OrderDTO> getAll()
    {
        return (List<OrderDTO>) (Object) DatabaseManager.selectValues("SELECT * FROM Orders ORDER BY id", "Orders", new LinkedHashMap<>());
    }

    public static boolean createOrder(String name_procedure, LinkedHashMap<String, Object> mapArgs)
    {
        return DatabaseManager.executeFunction("{? = call " + name_procedure + "(?, ?, ?)}", mapArgs);
    }

    @SuppressWarnings("unchecked")
    public static List<OrderDTO> getAllAsSeller(LinkedHashMap<String, Object> mapArgs)
    {
        return (List<OrderDTO>) (Object) DatabaseManager.selectValues("SELECT * FROM Orders WHERE userV = ? ORDER BY date_order DESC", "Orders", mapArgs);
    }

    @SuppressWarnings("unchecked")
    public static List<OrderDTO> getAllAsBuyer(LinkedHashMap<String, Object> mapArgs)
    {
        return (List<OrderDTO>) (Object) DatabaseManager.selectValues("SELECT * FROM Orders WHERE userC = ? ORDER BY date_order DESC", "Orders", mapArgs);
    }
}
