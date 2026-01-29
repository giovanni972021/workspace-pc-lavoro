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
                "<h1>Gestione Database Progetti</h1>" +
                "<p style='color:gray;'>Filtro: Status 1 oppure Status 0 modificati dopo 31/12/2023</p>" +
                "<div style='margin-bottom: 20px;'>" +
                "  <a href='/projects/table'><button style='padding:10px; margin:5px;'>Visualizza Tabella</button></a>"
                +
                "  <a href='/api/projects/download'><button style='padding:10px; margin:5px; background:#6f42c1; color:white;'>Scarica JSON Albero</button></a>"
                +
                "</div>" +
                "<hr><div><a href='/users/table'>Vai a Tabella Utenti</a></div></body></html>";
    }

    @GetMapping("/projects/table")
    public String getAllProjectsTable() {
        return buildProjectTable(userService.getAllProjectsSorted(), "Progetti Filtrati");
    }

    private String buildProjectTable(List<Project> projects, String title) {
        StringBuilder html = new StringBuilder();
        html.append(
                "<html><head><style>table { width: 95%; border-collapse: collapse; margin: 20px auto; font-family: Arial; } th { background-color: #28a745; color: white; padding: 10px; } td { padding: 8px; border: 1px solid #ddd; } .status-zero { color: #888; font-style: italic; }</style></head><body>")
                .append("<h2 style='text-align:center;'>").append(title).append("</h2>")
                .append("<div style='text-align:center;'><a href='/'>Torna alla Home</a></div>")
                .append("<table><tr><th>ID</th><th>Parent ID</th><th>Codice</th><th>Nome</th><th>Status</th><th>Ultima Modifica</th></tr>");

        for (Project p : projects) {
            String rowClass = "0".equals(p.getStatus()) ? "class='status-zero'" : "";
            html.append("<tr ").append(rowClass).append(">")
                    .append("<td>").append(p.getId()).append("</td>")
                    .append("<td>").append(p.getParentId() != null ? p.getParentId() : "<i>root</i>").append("</td>")
                    .append("<td>").append(p.getCode() != null ? p.getCode() : "").append("</td>")
                    .append("<td>").append(p.getName()).append("</td>")
                    .append("<td>").append(p.getStatus()).append("</td>")
                    .append("<td>").append(p.getModificationDateTime()).append("</td></tr>");
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