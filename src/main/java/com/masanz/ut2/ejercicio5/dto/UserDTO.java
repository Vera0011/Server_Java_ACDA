package com.masanz.ut2.ejercicio5.dto;

import com.masanz.ut2.ejercicio5.dao.ProductDAO;
import com.masanz.ut2.ejercicio5.dao.UserDAO;

import java.math.BigDecimal;
import java.util.*;

public class UserDTO
{
    private int id;
    private String full_name;
    private String user_;
    private String email;
    private String password_;
    private String creation_date;
    private String modification_date;
    private BigDecimal balance;

    public UserDTO(int id, String full_name, String user_, String email, String password_, String creation_date, String modification_date, BigDecimal balance) {
        this.id = id;
        this.full_name = full_name;
        this.user_ = user_;
        this.email = email;
        this.password_ = password_;
        this.creation_date = creation_date;
        this.modification_date = modification_date;
        this.balance = balance;
    }

    /* Manual creation */
    public UserDTO(String full_name, String user_, String email, String password_) {
        this.full_name = full_name;
        this.user_ = user_;
        this.email = email;
        this.password_ = password_;
    }

    /* Auth */
    public UserDTO(String user_, String password_)
    {
        this.user_ = user_;
        this.password_ = password_;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_() {
        return user_;
    }

    public void setUser_(String user_) {
        this.user_ = user_;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_() {
        return password_;
    }

    public void setPassword_(String password_) {
        this.password_ = password_;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getModification_date() {
        return modification_date;
    }

    public void setModification_date(String modification_date) {
        this.modification_date = modification_date;
    }

    public UserDTO insertIntoDatabase()
    {
        LinkedHashMap<String, Object> columnNames = new LinkedHashMap<>();

        columnNames.put("full_name", this.full_name);
        columnNames.put("user_", this.user_);
        columnNames.put("email", this.email);
        columnNames.put("password_", this.password_);

        return UserDAO.insertIntoDatabase(columnNames);
    }

    public List<UserDTO> authUserExists()
    {
        LinkedHashMap<String, Object> columnNames = new LinkedHashMap<>();

        columnNames.put("user_", this.user_);
        columnNames.put("password_", this.password_);

        return UserDAO.authUserExists(columnNames);
    }

    public List<ProductDTO> getProductsFromUser()
    {
        LinkedHashMap<String, Object> columnNames = new LinkedHashMap<>();

        columnNames.put("id", this.id);

        return UserDAO.getProductsFromUser(columnNames);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", full_name='" + full_name + '\'' +
                ", user_='" + user_ + '\'' +
                ", email='" + email + '\'' +
                ", password_='" + password_ + '\'' +
                ", creation_date='" + creation_date + '\'' +
                ", modification_date='" + modification_date + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDTO)) return false;
        return getId() == userDTO.getId() && Objects.equals(getUser_(), userDTO.getUser_());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFull_name(), getUser_(), getEmail(), getPassword_(), getCreation_date(), getModification_date(), balance);
    }
}
