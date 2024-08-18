package br.com.wepdev.security;

import br.com.wepdev.model.Usuario;
import br.com.wepdev.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findUserByLogin(username);/* Recebe o login pra consulta */

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não foi encontrado");
        }
        return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
    }

}