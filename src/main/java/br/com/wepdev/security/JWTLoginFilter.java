package br.com.wepdev.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.wepdev.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;


public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * Construtor
     * @param url essa url vem da requisicao
     * @param authenticationManager
     */
    public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {

        /*Obriga a autenticar a url*/
        super(new AntPathRequestMatcher(url));

        /*Gerenciador de autenticao*/
        setAuthenticationManager(authenticationManager);

    }


    /*Retorna o usuário ao processr a autenticacao*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // Fazendo a conversão da request para um tipo Usuario
        Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        /*Retorna o user com login e senha*/
        return getAuthenticationManager().
                authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        try {
            // Chamando o servico criado por mim que adiciona e valida o token
            new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo de falha ao buscar um usuario
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        if (failed instanceof BadCredentialsException) {
            response.getWriter().write("User e senha não encontrado");
        }else {
            response.getWriter().write("Falha ao logar: " + failed.getMessage());
        }
        //super.unsuccessfulAuthentication(request, response, failed);
    }
}
