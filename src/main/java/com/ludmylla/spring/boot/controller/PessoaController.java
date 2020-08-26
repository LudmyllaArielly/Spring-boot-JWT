package com.ludmylla.spring.boot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.OrderBy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.ludmylla.spring.boot.model.Pessoa;
import com.ludmylla.spring.boot.repository.PessoaRepository;
import com.ludmylla.spring.boot.repository.ProfissaoRepository;
import com.ludmylla.spring.boot.repository.TelefoneRepository;
import com.ludmylla.spring.boot.util.ReportsUtil;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private ReportsUtil<Pessoa> reportUtil;
	
	@Autowired
	private ProfissaoRepository profissaoRepository;
	
	/*
	 * Método para acessar a página de cadastro
	 * */
	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

		modelAndView.addObject("pessoaobj", new Pessoa());
		modelAndView.addObject("pessoas",  pessoaRepository.findAll(PageRequest.of(0, 5,Sort.by("nome"))));
		modelAndView.addObject("profissoes", profissaoRepository.findAll()); //Lista combo profissoes
		
		return modelAndView;
	}
	
	@GetMapping("/pessoaspag")
	public ModelAndView carregaPessoasPorPaginacao(@PageableDefault(size = 5) Pageable pageable,
			ModelAndView model, @RequestParam("nomepequisa") String nomepequisa) {
		
		Page<Pessoa> pagePessoa = pessoaRepository.findByNamePage(nomepequisa, pageable);
		model.addObject("pessoas", pagePessoa);
		model.addObject("pessoaobj", new Pessoa());
		model.addObject("nomepequisa", nomepequisa);
		model.setViewName("cadastro/cadastropessoa");
		return model;		
	}
	
	
	/*
	 * Método que salva e carrega a lista de pessoas cadastradas
	 * */
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa", consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult,
			final MultipartFile file) throws IOException {
		
		System.out.println(file.getContentType());
		System.out.println(file.getOriginalFilename());

		
		//Antes de editar ou salvar, vai carregar dos telefones da pessoa
		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
		
		if(bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			modelAndView.addObject("pessoas",  pessoaRepository.findAll(PageRequest.of(0, 5,Sort.by("nome"))));
			modelAndView.addObject("pessoaobj", pessoa);
			
			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage()); //vem das anotações @Notull
			}
			
			modelAndView.addObject("msg", msg);
			//carrega profissoes
			modelAndView.addObject("profissoes", profissaoRepository.findAll());
			return modelAndView;			
		}
		
			if(file.getSize() > 0) {/*Cadastrando curriculo*/
				pessoa.setCurriculo(file.getBytes());
				pessoa.setNomeFileCurriculo(file.getOriginalFilename());
				pessoa.setTipoFileCurriculo(file.getContentType());
			}else {
				if(pessoa.getId() != null && pessoa.getId() > 0){//editando
					
					Pessoa pessoaTemp = pessoaRepository.findById(pessoa.getId()).get();
					
					pessoa.setCurriculo(pessoaTemp.getCurriculo());
					pessoa.setNomeFileCurriculo(pessoaTemp.getNomeFileCurriculo());
					pessoa.setTipoFileCurriculo(pessoaTemp.getTipoFileCurriculo());
				}
			}
		
			pessoaRepository.save(pessoa);
			
			ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
			andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5,Sort.by("nome"))));
			andView.addObject("pessoaobj", new Pessoa());
			//carrega profissoes
			andView.addObject("profissoes", profissaoRepository.findAll());
					
			return andView;
	}
	
	/*
	 * Método que lista todas pessoas cadastradas no banco
	 * */
	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	@OrderBy("pessoasIt ASC")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");

		andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5,Sort.by("nome"))));
		//Passar objeto vazio
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	} 
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {

		//Retorna para tela de cadastro
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
				
		//Carregar o objeto pessoa usando o repository
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		
		//Passa o objeto para tela para fica em edição
		modelAndView.addObject("pessoaobj" , pessoa.get());
		//carrega profissoes
		modelAndView.addObject("profissoes", profissaoRepository.findAll());
		
		return modelAndView;
	}
	
	@GetMapping("/excluirpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {
		
		pessoaRepository.deleteById(idpessoa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5,Sort.by("nome"))));
		
		//Objeto vazio
		modelAndView.addObject("pessoaobj" , new Pessoa());
		//carrega profissoes
	    modelAndView.addObject("profissoes", profissaoRepository.findAll());
		
		return modelAndView;
	}
	
	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(
			@RequestParam("nomepequisa") String nomepequisa,
			@RequestParam("pesquisaSexo") String pesquisaSexo,
			@PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {		
		
		Page<Pessoa> pessoas = null;
		
		if(pesquisaSexo != null && !pesquisaSexo.isEmpty() &&
				nomepequisa != null && !nomepequisa.isEmpty()) {
			
			pessoas = pessoaRepository.findByNameSexoPage(nomepequisa, pesquisaSexo, pageable);
		
		}else if(nomepequisa != null && !nomepequisa.isEmpty()) {
			
			pessoas = pessoaRepository.findByNamePage(nomepequisa, pageable);
			
		}else if(pesquisaSexo != null && !pesquisaSexo.isEmpty()) {
			
			pessoas = pessoaRepository.findBySexoPage(pesquisaSexo, pageable);
		}
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", pessoas);//ver depois se colocar paginação
		modelAndView.addObject("pessoaobj", new Pessoa());
		modelAndView.addObject("nomepequisa", nomepequisa);
		//carrega profissoes
	    modelAndView.addObject("profissoes", profissaoRepository.findAll());
		return modelAndView;
	}
	
	@GetMapping("**/pesquisarpessoa")
	public void imprimiPdf(
			@RequestParam("nomepequisa") String nomepequisa,
			@RequestParam("pesquisaSexo") String pesquisaSexo,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception{
		
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		
		if(pesquisaSexo != null && !pesquisaSexo.isEmpty()
				&& nomepequisa != null && !nomepequisa.isEmpty()) {
			
			pessoas = pessoaRepository.findPessoaByNameSexo(nomepequisa, pesquisaSexo);
			
		}else if(nomepequisa != null && !nomepequisa.isEmpty()) {
			
			pessoas = pessoaRepository.findPessoaByName(nomepequisa);
			
		}else if(pesquisaSexo != null && !pesquisaSexo.isEmpty()) {
			
			pessoas = pessoaRepository.findPessoaBySexo(pesquisaSexo);
		}		
		else {			
			pessoas = (List<Pessoa>) pessoaRepository.findAll();
		}
		
		/*Chama serviço que faz a geração do relátorio*/
		byte[] pdf = reportUtil.gerarRelatorio(
				pessoas, "pessoa", request.getServletContext());
		
		/*Tamanho da resposta para o navegador*/
		response.setContentLength(pdf.length);
		
		/*Definir na resposta o tipo de arquivo*/
		response.setContentType("application/octet-stream");
		
		/*Definir o cabecalho da resposta*/
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		
		/*Finaliza resposta para o navegador*/
		response.getOutputStream().write(pdf);
		
	}	
	
	@GetMapping("**/downloadCurriculo/{idpessoa}")
	public void baixarCurriculo(@PathVariable("idpessoa") Long idpessoa,
			HttpServletResponse response) throws IOException {
		
		/*Consultar objeto pessoa no banco de dados*/
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		
		if(pessoa.getCurriculo() != null) {
			
			/*Setar o tamanho da resposta*/
			response.setContentLength(pessoa.getCurriculo().length);
			
			/*Tipo do arquivo para download ou pode ser generica application/octet-stream*/
			response.setContentType(pessoa.getTipoFileCurriculo());
			
			/*Define o cabeçalho da resposta*/
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeFileCurriculo());
			response.setHeader(headerKey, headerValue);
			
			/*Finaliza a resposta passando o arquivo*/
			response.getOutputStream().write(pessoa.getCurriculo());
		}
		
	}
}
