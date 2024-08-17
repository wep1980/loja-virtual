package br.com.wepdev.service;

import br.com.wepdev.model.Acesso;
import br.com.wepdev.repository.AcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcessoService {
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	
	public Acesso save(Acesso acesso) {
		
		/*Qualquer tipo de validação*/
		
		return acessoRepository.save(acesso);
	}

}
