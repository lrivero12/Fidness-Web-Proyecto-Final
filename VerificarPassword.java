
package com.fidness.web.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class VerificarPassword {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String password = "fidness12";

        String hash = "$2a$10$baYo9VDm9g/iR4q7cvoJwup85PW.B9UuU8IK8v5FVrMLNI6LALDgC";

        boolean coincide = encoder.matches(password, hash);

        if (coincide) {
            System.out.println("CONTRASEÑA CORRECTA");
        } else {
            System.out.println("CONTRASEÑA INCORRECTA");
        }
    }
}
