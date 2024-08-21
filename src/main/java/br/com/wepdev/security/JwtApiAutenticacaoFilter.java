package br.com.wepdev.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/*Filtro onde todas as requisicoes serão capturadas para autenticar*/
public class JwtApiAutenticacaoFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        /*Chamando o servico criado por mnim que estabece a autenticao do user*/

        Authentication authentication = new JWTTokenAutenticacaoService().
                // ServletRequest e ServletResponse sao interfaces, então e obrigatorio fazer um CAST(conversao)
                getAuthetication((HttpServletRequest) request, (HttpServletResponse) response);

        /*Coloca o processo de autenticacao para o spring secutiry*/
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);

    }
}
