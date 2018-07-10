package controllers;

import db.DBHelper;
import models.Department;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;


public class ManagersController {

    public ManagersController() {
        this.setupEndPoints();

    }


    private void setupEndPoints() {
            /* Require and result */
        get("/managers", (req, res) -> {

            /* We create a new HashMap taking in a String and an object and we call it models*/
            HashMap<String, Object> model = new HashMap<>();

            /* We then use a list with a manager type called managers and call a method using the DBHelper .get all and pass in manager.class*/
            List<Manager> managers = DBHelper.getAll(Manager.class);

            /* We then use .put on the "model" we created and its telling it what template to use, its in the layout.vtl*/
            model.put("template", "templates/managers/manager.vtl");


            /* model.put managers is using that list of managers we brought in from the DBHelper so we can use it in our html and loop
               over it etc. */
            model.put("managers", managers);

            /* We load our layout file and pass the keys and values we set in the "model".*/
            return new ModelAndView(model, "templates/layout.vtl");

            /*Velocity is a Java-based template engine that provides a template language to reference objects defined in Java code.*/
        }, new VelocityTemplateEngine());



        get("/managers/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);
            model.put("template", "templates/managers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());


        post("/managers", (req, res) -> {
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));
            int budget = Integer.parseInt(req.queryParams("budget"));
            Manager newManager = new Manager(firstName, lastName, salary, department, budget);
            DBHelper.save(newManager);
            res.redirect("/managers");
            return null;

        }, new VelocityTemplateEngine());

        get("/managers/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params("id"));
            Manager manager = DBHelper.find(id, Manager.class);
            model.put("manager", manager);
            model.put("template", "templates/managers/show.vtl");
            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());

        get("/managers/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params("id"));
            Manager manager = DBHelper.find(id, Manager.class);
            model.put("manager", manager);
            model.put("template", "templates/managers/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());

        post("/managers/:id", (req, res) -> {

            int id = Integer.parseInt(req.params("id"));
            Manager manager = DBHelper.find(id, Manager.class);

            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);

            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            int salary = Integer.parseInt(req.queryParams("salary"));
            double budget = Integer.parseInt(req.queryParams("budget"));

            manager.setFirstName(firstName);
            manager.setLastName(lastName);
            manager.setSalary(salary);
            manager.setBudget(budget);
            manager.setDepartment(department);

            DBHelper.save(manager);

            res.redirect("/managers");
            return null;
        }, new VelocityTemplateEngine());

    }

}