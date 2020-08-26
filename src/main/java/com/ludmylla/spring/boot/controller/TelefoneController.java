package com.ludmylla.spring.boot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ludmylla.spring.boot.model.Pessoa;
import com.ludmylla.spring.boot.model.Telefone;
import com.ludmylla.spring.boot.repository.PessoaRepository;
import com.ludmylla.spring.boot.repository.TelefoneRepository;

@Controller
public class TelefoneController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@RequestMapping(method =  RequestMethod.GET, value = "/telefones")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");

		modelAndView.addObject("telefoneobj" , new Telefone());
		
		return modelAndView;
	}
	
	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj" , pessoa.get());
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		
		return modelAndView;
	}
	
	@PostMapping("**/addfonepessoa/{pessoaid}")
	public ModelAndView addFonePessoa(Telefone telefone, @PathVariable("pessoaid")Long pessoaid) {
		
		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
		
		if(telefone!= null && telefone.getNumero().isEmpty() || telefone.getTipo()== null) {
			
		
			//Tela que vai retornar
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			
			//Retorna o objeto pessoa para mostra os dados na tela
			modelAndView.addObject("pessoaobj" , pessoa);
			
			//Retorna a lista de telefones
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
			
			//Cria um lista para mostrar erros
			List<String> msg = new ArrayList<String>();
	
			if(telefone.getNumero().isEmpty()) {
				msg.add("Número de telefone deve ser informado!");
			}
			
			else if(telefone.getTipo() == null) {
				msg.add("Tipo de telefone deve ser informado!");
			}
			else if((telefone.getTipo() == null) && (telefone.getNumero().isEmpty())) {
				msg.add("Número e tipo de telefone deve ser informado!");
			}
			
			modelAndView.addObject("msg",msg);
			return modelAndView;
			
		}else {
			List<String> msg = new ArrayList<String>();
			msg.add("Número e tipo de telefone deve ser informado!");			
		}
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");	
		
		telefone.setPessoa(pessoa);
		telefoneRepository.save(telefone);

		modelAndView.addObject("pessoaobj" , pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
		return modelAndView;
	}
	
	@GetMapping("/excluirtelefone/{idtelefone}")
	public ModelAndView excluirTelefone(@PathVariable("idtelefone") Long idtelefone) {
		
		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
		
		telefoneRepository.deleteById(idtelefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj" , pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));

		return modelAndView;
	}
	
	@GetMapping("editartelefone/{idtelefone}")
	public ModelAndView editar(@PathVariable("idtelefone") Long idtelefone) {

		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
		
		
		Optional<Telefone> telefone = telefoneRepository.findById(idtelefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
	
		modelAndView.addObject("pessoaobj" , pessoa);
		modelAndView.addObject("telefoneobj" , telefone.get());
	
		return modelAndView;
	}
}
