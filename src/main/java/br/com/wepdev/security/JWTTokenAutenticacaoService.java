package br.com.wepdev.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.wepdev.ApplicationContextLoad;
import br.com.wepdev.model.Usuario;
import br.com.wepdev.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

        liberacaoCors(response); // Liberando o CORS para os navegadores

        /*Usado para ver no Postman para teste. ENVIADO NO CORPO DA RESPOSTA */
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }


    /*Retorna o usuário validado com token ou caso nao seja valido retona null*/
    public Authentication getAuthetication(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader(HEADER_STRING);
        if (token != null) {

            String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim(); // retirando do token o Bearer

            /*Faz a validacao do token do usuário na requisicao e obtem o USER*/
            String user = Jwts.parser().
                    setSigningKey(SECRET)// essa assinatura aqui pode ser uma chave, um texto, certificado digital, etc...
                    .parseClaimsJws(tokenLimpo)
                    .getBody()
                    .getSubject(); //Pegando o usuario
            if (user != null) {
                Usuario usuario = ApplicationContextLoad.
                        getApplicationContext().
                        getBean(UsuarioRepository.class).findUserByLogin(user); // Buscando o usuario no banco de dados
                if (usuario != null) {
                    return new UsernamePasswordAuthenticationToken(
                            usuario.getLogin(),
                            usuario.getSenha(),
                            usuario.getAuthorities());
                }
            }
        }
        liberacaoCors(response);
        return null;
    }


    /**
     * Corrigindo erro de CORS ao utilizar um navegador para acessar os endpoints
     */
    private void liberacaoCors(HttpServletResponse response) {

        if (response.getHeader("Access-Control-Allow-Origin") == null) {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        if (response.getHeader("Access-Control-Allow-Headers") == null) {
            response.addHeader("Access-Control-Allow-Headers", "*");
        }
        if (response.getHeader("Access-Control-Request-Headers") == null) {
            response.addHeader("Access-Control-Request-Headers", "*");
        }
        if (response.getHeader("Access-Control-Allow-Methods") == null) {
            response.addHeader("Access-Control-Allow-Methods", "*");
        }
    }
}
