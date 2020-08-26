package com.ludmylla.spring.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	@Override // Configura as solicitações de acesso por Http
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf()
	    .disable() // Desativa as configurações padrão de memória
		 .authorizeRequests() // Permite restringir acesssos
		 .antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a pagina inicial
		 .antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN")
		 .anyRequest().authenticated()
		 .and().formLogin().permitAll() //Permite qualquer usuário
		 .loginPage("/login")
		 .defaultSuccessUrl("/cadastropessoa")
		 .failureUrl("/login?error=true")
		 .and().logout()
		 .logoutSuccessUrl("/login")//Mapeia url de Logout e invalida usuário autenticado
		 .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
	}
	
	@Override //Cria autenticação do usuário com banco de dados ou em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(implementacaoUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		
		/*auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
		.withUser("lud")
		.password("$2a$10$.7ilrtbRyrQ/o1WWNpG9ou0cJv9EE3dxPaLC/XBt9EAsaGlmMxei2")
		.roles("ADMIN");*/
	}
	
	@Override //Ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("src/main/resources/**");		 
		//web.ignoring().antMatchers("/materialize/**");
		web.ignoring().antMatchers("/css/**");
		web.ignoring().antMatchers("/js/**");
		web.ignoring().antMatchers("/img/**");
		
	}	
	
}
