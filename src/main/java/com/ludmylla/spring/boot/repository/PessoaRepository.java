package com.ludmylla.spring.boot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.ludmylla.spring.boot.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	@Query("select p from Pessoa p where  lower(p.nome) like lower(concat('%', ?1,'%'))")
	List<Pessoa> findPessoaByName(String nome);
	
	@Query("select p from Pessoa p where p.sexo = ?1")
	List<Pessoa> findPessoaBySexo(String sexo);
	
	@Query("select p from Pessoa p where  lower(p.nome) like lower(concat('%', ?1,'%')) and p.sexo = ?2")
	List<Pessoa> findPessoaByNameSexo(String nome, String sexo);
	
	
	// PAGINAÇÃO (pesquisa pessoa - NOME)
	default Page<Pessoa> findByNamePage(String nome, Pageable pageable){
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		
		// Configurando pesquisa para consulta por partes do nome no banco (análogo ao LIKE do SQl)
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		// Une o objeto com o valor e a configuração para consultar
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
		
		return findAll(example, pageable);
	}
	
	// PAGINAÇÃO (pesquisa pessoa - SEXO)
		default Page<Pessoa> findBySexoPage(String sexo, Pageable pageable){
			
			Pessoa pessoa = new Pessoa();
			pessoa.setSexo(sexo);
			
			// Configurando pesquisa para consulta por partes do nome no banco (análogo ao LIKE do SQl)
			ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
					.withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
			
			// Une o objeto com o valor e a configuração para consultar
			Example<Pessoa> example = Example.of(pessoa, exampleMatcher);		
			
			return findAll(example, pageable);
		}

	// PAGINAÇÃO (pesquisa pessoa - NOME e SEXO)
	default Page<Pessoa> findByNameSexoPage(String nome, String sexo,Pageable pageable){
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSexo(sexo);
		
		/*
		 * Estamos configurando a pesquisa para consultar
		 *  por partes do nome no banco de dados,
		 *   e igual a like com sql*
		 */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		/*Une o objeto com o valor e a configuração para consultar*/
		Example<Pessoa> example = Example.of(pessoa, exampleMatcher);

		return findAll(example, pageable);
	}


	
	
	

}
