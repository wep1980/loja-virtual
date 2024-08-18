package br.com.wepdev.security;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*Criar a autenticação e retonar também a autenticação JWT*/
@Service
@Component
public class JWTTokenAutenticacaoService {

    /*Token de validade de 11 dias*/
    private static final long EXPIRATION_TIME = 959990000;

    /*Chave de senha para juntar com o JWT*/
    private static final String SECRET = "ss/-*-*sds565dsd-s/d-s*dsds";

    //Prefixo do Token
    private static final String TOKEN_PREFIX = "Bearer";

    // Serve para pegar o token na requisicao
    private static final String HEADER_STRING = "Authorization";


    /*Gera o token e da a responsta para o cliente o com JWT*/
    public void addAuthentication(HttpServletResponse response, String username) throws Exception {
        /*Montagem do Token*/
        String JWT = Jwts.builder()./*Chama o gerador de token*/
                setSubject(username) /*Adiciona o user*/
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Tempo de expiracao do token
                .signWith(SignatureAlgorithm.HS512, SECRET) // Assinatura do token + nossa senha secreta
                .compact(); // compactando o token com todas as informações

        //Exemplo de token : Bearer *-/a*dad9s5d6as5d4s5d4s45dsd54s.sd4s4d45s45d4sd54d45s4d5s.ds5d5s5d5s65d6s6d*
        String token = TOKEN_PREFIX + " " + JWT;

        /*Dá a resposta pra tela e para o cliente, outra API, navegador, aplicativo, javascript, outra chamada java*/
        response.addHeader(HEADER_STRING, token);

        /*Usado para ver no Postman para teste. ENVIADO NO CORPO DA RESPOSTA */
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

    }
}
