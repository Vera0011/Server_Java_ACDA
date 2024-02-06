package com.masanz.ut2.ejercicio5;

import com.masanz.ut2.ejercicio5.dao.OrderDAO;
import com.masanz.ut2.ejercicio5.dao.UserDAO;
import com.masanz.ut2.ejercicio5.dto.UserDTO;
import com.masanz.ut2.ejercicio5.init.DatabaseManager;
import freemarker.template.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.swing.text.View;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class ServerMain {

    private static final Logger logger = LogManager.getLogger(ServerMain.class);
    private static UserDTO userDTO;

    public static void main(String[] args) {

        DatabaseManager.initDatabase("root", "root");

        staticFileLocation("/public");
        port(8080);

        FreeMarkerEngine freeMarker = new FreeMarkerEngine();
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        configuration.setClassForTemplateLoading(ServerMain.class, "/templates");
        freeMarker.setConfiguration(configuration);

        get("/", (request, response) -> new ModelAndView(null, "login.ftl"), freeMarker);

        get("/home", (request, response) -> {
            response.redirect("/home.html");
            return null;
        });

        get("/logout", (request, response) -> {
            userDTO = null;
            return new ModelAndView(null, "login.ftl");
        }, freeMarker);

        post("/login", (request, response) -> {

            String username = request.queryParams("username");
            String password = request.queryParams("password");
            userDTO = new UserDTO(username, password);
            List<UserDTO> listUserAuth = userDTO.authUserExists();

            if (listUserAuth.size() == 1)
            {
                response.redirect("/home.html");
                userDTO = listUserAuth.get(0);
                logger.info("New user logged in: " + username);
            }
            else
            {
                response.redirect("/");
            }
            return null;
        });

        get("/articulos", (request, response) -> {
            if(userDTO != null)
            {
                Map<String, Object> model = new HashMap<>();

                model.put("user_logged", userDTO.getId());
                model.put("usuario", userDTO);
                model.put("articulos", userDTO.getProductsFromUser());

                return new ModelAndView(model, "articulos.ftl");
            }
            else return new ModelAndView(null, "login.ftl");
        }, freeMarker);

        get("/articulos/:id", (request, response) -> {
            if(userDTO != null)
            {
                int userId = Integer.parseInt(request.params(":id"));
                Map<String, Object> model = new HashMap<>();
                LinkedHashMap<String, Object> mapArgs = new LinkedHashMap<>();

                mapArgs.put("id", userId);

                model.put("user_logged", userDTO.getId());
                model.put("usuario", UserDAO.getByID(mapArgs));
                model.put("articulos", UserDAO.getProductsFromUser(mapArgs));

                return new ModelAndView(model, "articulos.ftl");
            }
            else return new ModelAndView(null, "login.ftl");
        }, freeMarker);

        get("/usuarios", (request, response) -> {
            if(userDTO != null)
            {
                Map<String, Object> model = new HashMap<>();
                List<UserDTO> listUsers = UserDAO.getAll();
                int i = 0;

                /* Remove actual user (not listing it) */
                while(i < listUsers.size())
                {
                    if(listUsers.get(i).equals(userDTO)){ listUsers.remove(i); i--;};
                    i++;
                }

                model.put("users", listUsers);

                return new ModelAndView(model, "usuarios.ftl");
            }
            else return new ModelAndView(null, "login.ftl");
        }, freeMarker);

        get("/usuario", (request, response) -> {
            if(userDTO != null)
            {
                Map<String, Object> model = new HashMap<>();
                LinkedHashMap<String, Object> idToSearch = new LinkedHashMap<>();

                idToSearch.put("id", userDTO.getId());

                model.put("usuario", UserDAO.getByID(idToSearch));

                return new ModelAndView(model, "usuario.ftl");
            }
            else return new ModelAndView(null, "login.ftl");
        }, freeMarker);

        get("/usuario/:id", (request, response) -> {
            if(userDTO != null)
            {
                int userId = Integer.parseInt(request.params(":id"));
                Map<String, Object> model = new HashMap<>();
                LinkedHashMap<String, Object> mapArgs = new LinkedHashMap<>();

                mapArgs.put("id", userId);
                model.put("usuario", UserDAO.getByID(mapArgs));

                return new ModelAndView(model, "usuario.ftl");
            }
            else return new ModelAndView(null, "login.ftl");
        }, freeMarker);

        get("/comprar/:id_user/:id_product", (request, response) -> {
            if(userDTO != null)
            {
                int articuloId = Integer.parseInt(request.params(":id_product"));
                int userSeller = Integer.parseInt(request.params(":id_user"));
                LinkedHashMap<String, Object> createOrderArgs = new LinkedHashMap<>();
                Map<String, Object> model = new HashMap<>();

                createOrderArgs.put("objectID", articuloId);
                createOrderArgs.put("userC", userDTO.getId());
                createOrderArgs.put("userV", userSeller);

                OrderDAO.createOrder("create_order", createOrderArgs);
                model.put("user_logged", userDTO.getId());
                model.put("usuario", userDTO);
                model.put("articulos", userDTO.getProductsFromUser());

                return new ModelAndView(model, "articulos.ftl");
            }
            else return new ModelAndView(null, "login.ftl");
        }, freeMarker);

        get("/registro", (request, response) -> {
            if(userDTO != null)
            {
                Map<String, Object> model = new HashMap<>();
                LinkedHashMap<String, Object> idMap = new LinkedHashMap<>();

                idMap.put("id", userDTO.getId());

                model.put("usuario", userDTO);
                model.put("ventas", OrderDAO.getAllAsSeller(idMap));
                model.put("compras", OrderDAO.getAllAsBuyer(idMap));

                return new ModelAndView(model, "registro.ftl");
            }
            else return new ModelAndView(null, "login.ftl");
        }, freeMarker);

        init();
    }
}