package br.com.wepdev;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenhaBCrypt {

    public static void main(String[] args) {

        System.out.println(new BCryptPasswordEncoder().encode("123"));
    }
}
