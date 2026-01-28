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
                "<h1>Gestione Database</h1>" +
                "<a href='/users/table'><button style='padding:10px; cursor:pointer;'>Tabella Utenti</button></a> " +
                "<a href='/projects/table'><button style='padding:10px; cursor:pointer; background:#28a745; color:white;'>Tabella Progetti Ordinata</button></a>"
                +
                "</body></html>";
    }

    // --- PROGETTI ---
    @GetMapping("/projects") // JSON grezzo
    public List<Project> getProjectsJson() {
        return userService.getAllProjectsSorted();
    }

    @GetMapping("/projects/table") // Tabella Ordinata
    public String getProjectsTable() {
        List<Project> projects = userService.getAllProjectsSorted();
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
                .append("table { width: 90%; border-collapse: collapse; margin: 20px auto; font-family: Arial; }")
                .append("th, td { padding: 12px; border: 1px solid #ddd; text-align: left; }")
                .append("th { background-color: #28a745; color: white; }")
                .append("tr:nth-child(even) { background-color: #f2f2f2; }")
                .append("</style></head><body>")
                .append("<h2 style='text-align:center;'>Progetti (Ordinati per ID e ParentID)</h2>")
                .append("<table><tr><th>ID</th><th>Parent ID</th><th>Nome</th><th>Status</th></tr>");

        for (Project p : projects) {
            html.append("<tr>")
                    .append("<td>").append(p.getId()).append("</td>")
                    .append("<td>").append(p.getParentId() != null ? p.getParentId() : "-").append("</td>")
                    .append("<td>").append(p.getName()).append("</td>")
                    .append("<td>").append(p.getStatus()).append("</td>")
                    .append("</tr>");
        }
        html.append(
                "</table><div style='text-align:center;'><a href='/exportProjects'>Esporta in JSON</a></div></body></html>");
        return html.toString();
    }

    @GetMapping("/exportProjects")
    public String exportProjects() {
        try {
            objectMapper.writeValue(new File("projects.json"), userService.getAllProjectsSorted());
            return "File projects.json creato con successo!";
        } catch (IOException e) {
            return "Errore: " + e.getMessage();
        }
    }

    // --- UTENTI ---
    @GetMapping("/users/table")
    public String getUsersTable() {
        List<User> users = userService.getAllUsers();
        StringBuilder html = new StringBuilder(
                "<html><head><style>table{width:80%;border-collapse:collapse;margin:auto;font-family:sans-serif;}th,td{padding:10px;border:1px solid #ccc;}th{background:#007bff;color:white;}</style></head><body>");
        html.append(
                "<h2 style='text-align:center;'>Tabella Utenti</h2><table><tr><th>ID</th><th>Nome</th><th>Email</th></tr>");
        for (User u : users) {
            html.append("<tr><td>").append(u.getId()).append("</td><td>").append(u.getName()).append("</td><td>")
                    .append(u.getEmail()).append("</td></tr>");
        }
        html.append("</table></body></html>");
        return html.toString();
    }
}