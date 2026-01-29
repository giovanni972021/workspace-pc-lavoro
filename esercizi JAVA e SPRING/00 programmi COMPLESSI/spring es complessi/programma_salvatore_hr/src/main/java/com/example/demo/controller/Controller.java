package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.Project;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- HOME PAGE ---
    @GetMapping("/")
    public String home() {
        return "<html><body style='font-family:sans-serif; text-align:center; padding:50px;'>" +
                "<h1>Gestione Database Progetti</h1>" +
                "<div style='margin-bottom: 20px;'>" +
                "  <a href='/projects/table'><button style='padding:10px; margin:5px;'>Tutti i Progetti</button></a>" +
                "  <a href='/projects/parent_id_null'><button style='padding:10px; margin:5px; background:#007bff; color:white;'>Solo commesse - ParentID Vuoto</button></a>"
                +
                "  <a href='/projects/parent_id_not_null'><button style='padding:10px; margin:5px; background:#17a2b8; color:white;'>Solo attivita delle commesse ParentID NON vuoto</button></a>"
                +
                "</div>" +
                "<hr>" +
                "<div><a href='/users/table'>Vai a Tabella Utenti</a></div>" +
                "</body></html>";
    }

    // --- ENDPOINT TABELLE ---

    @GetMapping("/projects/table")
    public String getAllProjectsTable() {
        return buildProjectTable(userService.getAllProjectsSorted(), "Tutti i Progetti");
    }

    @GetMapping("/projects/parent_id_null")
    public String getMainProjectsTable() {
        // Usa il nuovo metodo del service/repo per parentId IS NULL
        return buildProjectTable(userService.getMainProjects(), "Progetti Principali (Parent ID Vuoto)");
    }

    @GetMapping("/projects/parent_id_not_null")
    public String getSubProjectsTable() {
        // Usa il nuovo metodo del service/repo per parentId IS NOT NULL
        return buildProjectTable(userService.getSubProjects(), "Sotto-Progetti (Parent ID Presente)");
    }

    // Metodo privato per costruire l'HTML della tabella ed evitare ripetizioni
    private String buildProjectTable(List<Project> projects, String title) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
                .append("table { width: 95%; border-collapse: collapse; margin: 20px auto; font-family: Arial; font-size: 13px; }")
                .append("th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }")
                .append("th { background-color: #28a745; color: white; }")
                .append("tr:nth-child(even) { background-color: #f9f9f9; }")
                .append("</style></head><body>")
                .append("<h2 style='text-align:center;'>").append(title).append("</h2>")
                .append("<div style='text-align:center;'><a href='/'>Torna alla Home</a></div>")
                .append("<table><tr><th>ID</th><th>Parent ID</th><th>Codice</th><th>Nome</th><th>Status</th><th>Creazione</th><th>Modifica</th></tr>");

        for (Project p : projects) {
            html.append("<tr>")
                    .append("<td>").append(p.getId()).append("</td>")
                    .append("<td>").append(p.getParentId() != null ? p.getParentId() : "<i>vuoto</i>").append("</td>")
                    .append("<td>").append(p.getCode() != null ? p.getCode() : "").append("</td>")
                    .append("<td>").append(p.getName()).append("</td>")
                    .append("<td>").append(p.getStatus()).append("</td>")
                    .append("<td>").append(p.getCreationDateTime()).append("</td>")
                    .append("<td>").append(p.getModificationDateTime()).append("</td>")
                    .append("</tr>");
        }
        html.append("</table></body></html>");
        return html.toString();
    }

    @GetMapping("/exportProjects")
    public String exportProjects() {
        try {
            objectMapper.writeValue(new File("projects.json"), userService.getAllProjectsSorted());
            return "Esportazione completata!";
        } catch (IOException e) {
            return "Errore: " + e.getMessage();
        }
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