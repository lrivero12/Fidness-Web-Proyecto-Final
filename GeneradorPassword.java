
package com.fidness.web.security;

import java.io.Console;
import java.util.Arrays;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneradorPassword {

    public static void main(String[] args) {

        Console consola = System.console();

        if (consola == null) {
            System.err.println(
                "Ejecuta esta clase desde una terminal interactiva."
            );
            return;
        }

        char[] password = consola.readPassword(
            "Ingresa la contraseña del administrador: "
        );

        if (password == null || password.length == 0) {
            System.err.println("La contraseña no puede estar vacía.");
            return;
        }

        try {
            BCryptPasswordEncoder encoder =
                    new BCryptPasswordEncoder();

            String hash = encoder.encode(
                    new String(password)
            );

            System.out.println("HASH BCrypt:");
            System.out.println(hash);

        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
