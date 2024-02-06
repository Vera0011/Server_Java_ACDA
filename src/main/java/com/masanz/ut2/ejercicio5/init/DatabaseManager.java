package com.masanz.ut2.ejercicio5.init;

import com.masanz.ut2.ejercicio5.ServerMain;
import com.masanz.ut2.ejercicio5.dto.OrderDTO;
import com.masanz.ut2.ejercicio5.dto.ProductDTO;
import com.masanz.ut2.ejercicio5.dto.UserDTO;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static final Logger logger = LogManager.getLogger(ServerMain.class);
    protected static final String database_name = "app_server";
    protected static Connection sqlConnection;
    protected static String static_username;
    protected static String static_password;

    /**
     * Creates a connection to the database
     *
     * @param username Username of the user
     * @param password Password of the user
     */
    public static void initDatabase(String username, String password) {
        if (init(database_name, username, password)) {
            static_username = username;
            static_password = password;
        }
    }

    /**
     * Creates tables and inserts default users
     */
    private static boolean execute_creation() {
        if (init(database_name)) {
            try
            {
                ScriptRunner sr = new ScriptRunner(sqlConnection);
                sr.setStopOnError(true);

                Reader reader = new BufferedReader(new FileReader("src/main/java/com/masanz/ut2/ejercicio5/init/src/Init_SQL.sql"));
                Reader reader2 = new BufferedReader(new FileReader("src/main/java/com/masanz/ut2/ejercicio5/init/src/Inserts_SQL.sql"));

                sr.runScript(reader);
                sr.runScript(reader2);

                return true;
            } catch (FileNotFoundException e) {
                logger.error("No se encontró el archivo SQL: " + e.getMessage());
            } catch (RuntimeSqlException error) {
                logger.error("No se pudo cargar el archivo SQL: " + error.getMessage());
            }
        }

        return false;
    }

    /**
     * Closes the database
     */
    public static boolean close() {
        if (sqlConnection != null) {
            try {
                sqlConnection.close();
                sqlConnection = null;
            } catch (SQLException e) {
                logger.error("Error cerrando la base de datos: " + e);
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Overcharged function: Initiates database with already stored credentials
     *
     * @param database Database used
     */
    private static boolean init(String database) {
        return init(database, static_username, static_password);
    }

    /**
     * Initiates database. If database is not created, creates it
     *
     * @param database Database where we want the connection
     * @param username Username used in the connection
     * @param password Password used in the connection
     */
    private static boolean init(String database, String username, String password) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost/" + database, username, password);

            return true;
        } catch (NullPointerException e) {
            logger.error("Error cargando el driver: " + e);
        } catch (SQLException error) {
            if (error.getClass().getSimpleName().equals("SQLSyntaxErrorException")) {
                try {
                    sqlConnection = DriverManager.getConnection("jdbc:mysql://localhost/", username, password);

                    PreparedStatement st = sqlConnection.prepareStatement("CREATE DATABASE " + database);
                    st.executeUpdate();

                    static_username = username;
                    static_password = password;

                    init(database, username, password);
                    execute_creation();

                    st.close();

                    return true;
                } catch (SQLException err) {
                    logger.error("Error al conectar a la base de datos: " + err);
                }
            } else {
                logger.error("Error al conectar a la base de datos: " + error);
            }
        }

        return false;
    }

    public static Object insertValues(String sql_query, String table, LinkedHashMap<String, Object> mapArgs) {
        if (init(database_name)) {
            try
            {
                PreparedStatement st = addAtributesStatement(sqlConnection.prepareStatement(sql_query, Statement.RETURN_GENERATED_KEYS), mapArgs, 1);

                st.executeUpdate();
                ResultSet data = st.getGeneratedKeys();
                data.next();

                /* Retrieve SELECT to return object DTO*/
                LinkedHashMap<String, Object> columnNames = new LinkedHashMap<>();

                columnNames.put("id", data.getInt(1));

                data.close();
                st.close();

                return switch (table)
                {
                    case "Products" -> selectValues("SELECT * FROM Products WHERE id = ?", "Products", columnNames);
                    case "Orders" -> selectValues("SELECT * FROM Orders WHERE id = ?", "Orders", columnNames);
                    case "Users" -> selectValues("SELECT * FROM Users WHERE id = ?", "Users", columnNames);
                    default -> null;
                };
            }
            catch (SQLException err)
            {
                logger.error("Error al conectar a la base de datos: " + err);
            }
        }

        return null;
    }

    /* Add attributes to Statement */
    private static PreparedStatement addAtributesStatement(PreparedStatement st, LinkedHashMap<String, Object> mapArgs, int startIndex) throws SQLException {
        Iterator<Map.Entry<String, Object>> iterator = mapArgs.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<String, Object> data = iterator.next();

            if (data.getValue() instanceof Integer) st.setInt(startIndex, (Integer) data.getValue());
            else if (data.getValue() instanceof Double)
                st.setDouble(startIndex, (Double) data.getValue());
            else st.setString(startIndex, (String) data.getValue());

            startIndex++;
        }

        return st;
    }

    public static List<Object> selectValues(String sql_query, String table, LinkedHashMap<String, Object> mapArgs) {
        List<Object> result = new ArrayList<>();

        if (init(database_name))
        {
            try
            {
                PreparedStatement st = addAtributesStatement(sqlConnection.prepareStatement(sql_query), mapArgs, 1);

                ResultSet data = st.executeQuery();

                while (data.next())
                {
                    if (table.equals("Products")) result.add(new ProductDTO(data.getInt("id"), data.getString("name_"), data.getDouble("value_"), data.getInt("userID")));
                    else if (table.equals("Orders")) result.add(new OrderDTO(data.getInt("id"), data.getInt("objectID"), data.getInt("userC"), data.getInt("userV"), data.getString("date_order")));
                    else if (table.equals("Users")) result.add(new UserDTO(data.getInt("id"), data.getString("full_name"), data.getString("user_"), data.getString("email"), data.getString("password_"), data.getString("creation_date"), data.getString("modification_date"), data.getBigDecimal("balance")));
                }

                data.close();
                st.close();

                return result;
            }
            catch (SQLException err)
            {
                logger.error("Error al conectar a la base de datos: " + err);
            }
        }

        return result;
    }

    public static void removeDatabase()
    {
        if (init(database_name))
        {
            try {
                PreparedStatement st = sqlConnection.prepareStatement("DROP DATABASE IF EXISTS " + database_name);
                st.executeUpdate();

                st.close();
            } catch (SQLException e) {
                logger.error("Error al conectar a la base de datos: " + e);
            } finally {
                close();
            }
        }
    }

    public static boolean executeFunction(String sql_query, LinkedHashMap<String, Object> mapArgs)
    {
        if (init(database_name))
        {
            try
            {
                    sqlConnection.setAutoCommit(false);
                    CallableStatement callableStatement = (CallableStatement) addAtributesStatement(sqlConnection.prepareCall(sql_query), mapArgs, 2);
                    callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);

                    callableStatement.execute();
                    sqlConnection.setAutoCommit(true);

                    int result = callableStatement.getInt(1);

                    callableStatement.close();

                    return result == 1;
            }
            catch (SQLException err)
            {
                try
                {
                    if (sqlConnection != null)
                    {
                        sqlConnection.rollback();
                        logger.error("Rollback realizado correctamente");
                    }
                }
                catch (SQLException rollbackException)
                {
                    logger.error("Error al realizar el rollback: " + rollbackException.getMessage());
                }

                logger.error("Error al ejecutar la función: " + err.getMessage());
            }
        }

        return false;
    }
}
