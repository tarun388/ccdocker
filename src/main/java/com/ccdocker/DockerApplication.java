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

        //
        if (cmd.equals("run")) {
            try {
                // Construct the command to be executed within the new namespace
                StringBuilder command = new StringBuilder();
                // Rename the hostname of sub-process to 'container'
                // The goal here is launch the container with it’s own hostname,
                // whilst not affecting the hostname of the host operating system
                command.append("hostname container && ");
                // Isolate the processes running inside the container from the host filesystem
                // chroot: change the root file system
                command.append("chroot /mnt/hgfs/ccdocker/alpine-rootfs/ ");
                // Run arbitrary user program command
                for (int i = 1; i < args.length; i++) {
                    command.append(args[i]).append(" ");
                }

                // Create ProcessBuilder
                // Creates a new UTS namespace
                ProcessBuilder processBuilder = new ProcessBuilder("unshare", "--uts", "bash", "-c").inheritIO();
                // log.debug(command.toString());
                processBuilder.command().add(command.toString());

                // Start the process
                Process process = processBuilder.start();

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
