package com.ccdocker;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class DockerApplication {

    public static void main(String[] args) {
        log.debug("Starting ccrun");

        // Top level command
        String cmd = args[0];

        if (cmd.equals("run")) {
            try {
                // Create ProcessBuilder
                ProcessBuilder processBuilder = new ProcessBuilder();
                for (int i = 1; i < args.length; i++) {
                    processBuilder.command().add(args[i]);
                }

                // Start the process
                Process process = processBuilder.start();

                // Read output
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // Wait for the process to finish
                int exitCode = process.waitFor();
                log.debug("Exited with code: " + exitCode);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usage: ccrun [OPTIONS] COMMAND");
        }
    }
}
