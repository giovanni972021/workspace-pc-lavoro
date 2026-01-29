package com.example.demo.controller;

import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class Controller {

    private final UserService userService;

    public Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "<html><body style='font-family:sans-serif; text-align:center; padding:50px;'>" +
                "<h1>Gestione Progetti Salvatore HR</h1>" +
                "<div style='margin-bottom: 20px;'>" +
                "  <a href='/projects/table'><button style='padding:10px; margin:5px;'>Tabella HTML</button></a>" +
                "  <a href='/api/projects/download'><button style='padding:10px; margin:5px; background:#6f42c1; color:white;'>Scarica JSON (Kubernetes Ready)</button></a>"
                +
                "</div>" +
                "<hr><div><a href='/users/table'>Tabella Utenti</a></div></body></html>";
    }

    @GetMapping("/projects/table")
    public String getAllProjectsTable() {
        List<Project> projects = userService.getAllProjectsSorted();
        StringBuilder html = new StringBuilder(
                "<html><head><style>table { width: 95%; border-collapse: collapse; margin: 20px auto; font-family: Arial; } th { background-color: #007bff; color: white; padding: 10px; } td { padding: 8px; border: 1px solid #ddd; } tr:nth-child(even) { background-color: #f2f2f2; }</style></head><body>");
        html.append("<h2 style='text-align:center;'>Progetti nel Sistema</h2>")
                .append("<div style='text-align:center;'><a href='/'>Indietro</a></div>")
                .append("<table><tr><th>ID</th><th>Parent</th><th>Codice</th><th>Nome</th><th>Status</th><th>Modifica</th></tr>");

        for (Project p : projects) {
            html.append("<tr><td>").append(p.getId()).append("</td><td>").append(p.getParentId()).append("</td><td>")
                    .append(p.getCode()).append("</td><td>").append(p.getName()).append("</td><td>")
                    .append(p.getStatus()).append("</td><td>").append(p.getModificationDateTime()).append("</td></tr>");
        }
        html.append("</table></body></html>");
        return html.toString();
    }

    @GetMapping("/users/table")
    public String getUsersTable() {
        List<User> users = userService.getAllUsers();
        StringBuilder html = new StringBuilder(
                "<html><body><h2 style='text-align:center;'>Utenti</h2><table border='1' style='margin:auto;'><tr><th>ID</th><th>Nome</th><th>Email</th></tr>");
        for (User u : users) {
            html.append("<tr><td>").append(u.getId()).append("</td><td>").append(u.getName()).append("</td><td>")
                    .append(u.getEmail()).append("</td></tr>");
        }
        html.append("</table><p style='text-align:center;'><a href='/'>Home</a></p></body></html>");
        return html.toString();
    }
}