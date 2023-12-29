package com.example.gitlogwebapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Controller
public class GitLogWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitLogWebApplication.class, args);
    }

    @GetMapping("/log")
    public String getLog(@RequestParam(value = "path", required = false) String path, Model model) {
        if (path == null) {
            model.addAttribute("errMsg", "The path to the repository should be provided.");
            return "log";
        }
        ProcessBuilder pb = new ProcessBuilder("git", "log", "--all", "--decorate", "--oneline", "--graph");
        try {
            pb.directory(new File(path));
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            List<String> lines = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            model.addAttribute("lines", lines);
        } catch (IOException e) {
            model.addAttribute("errMsg", "There is no repository with the given path.");
        }
        return "log";
    }
}
