package gft.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import gft.dto.listaNoticias.ListaNoticiasDTO;
import gft.dto.usuario.RegistroEtiquetasDoUsuarioDTO;
import gft.entities.Etiqueta;
import reactor.core.publisher.Mono;

@Service
public class EnvioEmailService {
	
	private final WebClient webClient;
	private final JavaMailSender mailSender;
	
	public EnvioEmailService(WebClient webClient, JavaMailSender mailSender) {
		this.webClient = webClient;
		this.mailSender = mailSender;
	}

	public String enviarEmailDeNoticias(List<RegistroEtiquetasDoUsuarioDTO> registros, String date) {
		List<Object> registroNoticiasPorUsuario = new ArrayList<>();
		List<String> etiquetasDoUsuario = new ArrayList<>();
		String formatandoNoticiasDoUsuario = "";
		for (RegistroEtiquetasDoUsuarioDTO registro : registros) {
			for (Etiqueta etiqueta : registro.getEtiquetas()) {
				Mono<ListaNoticiasDTO> monoNoticia = consumirApiNoticias(etiqueta.getNome(), date);
				ListaNoticiasDTO noticias = monoNoticia.block();
				
				etiquetasDoUsuario.add(etiqueta.getNome());
				registroNoticiasPorUsuario.add(noticias.getList());
			}
			
			for(Object noticiasDoUsuario : registroNoticiasPorUsuario) {
				formatandoNoticiasDoUsuario = noticiasDoUsuario.toString();
			}

			String nomesEtiquetasFormatados = formatarExibicacaoTexto(etiquetasDoUsuario);
			String textoAssunto = "Resumo de notícias (" + date + ")";
			String textoCorpoEmail = "Tópicos, de acordo com o cadastro de etiquetas: " + nomesEtiquetasFormatados + ".\n\n\nNotícias:" + formatandoNoticiasDoUsuario.substring(1);
			enviarEmailParaUsuario(registro.getEmail(), textoAssunto, textoCorpoEmail);

			limparLists(etiquetasDoUsuario, registroNoticiasPorUsuario);
		}
		
		return formatandoNoticiasDoUsuario;
	}
	
	public Mono<ListaNoticiasDTO> consumirApiNoticias(String nomeEtiqueta, String date) {
		return this.webClient.method(HttpMethod.GET)
		.uri(uriBuilder -> uriBuilder
				.path("api/")
				.queryParam("q", nomeEtiqueta)
				.queryParam("date", date).build())
		.retrieve()
		.bodyToMono(ListaNoticiasDTO.class);
	}

	public String formatarExibicacaoTexto(List<String> lista) {
		int i = 0;
		String fraseFormatada = "";
		
		for (String indice : lista) {
			if (i == 0) {
				fraseFormatada = indice;
				i++;
			} else {
				fraseFormatada = fraseFormatada + ", " + indice;
			}
		}
		
		return fraseFormatada.toLowerCase();
	}
	
	public void enviarEmailParaUsuario(String email, String textoAssunto, String textoCorpoEmail) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject(textoAssunto);
		message.setText(textoCorpoEmail);

		mailSender.send(message);
	}
	
	public void limparLists(List<String> etiquetasDoUsuario, List<Object> registroNoticiasPorUsuario) {
		etiquetasDoUsuario.clear();
		registroNoticiasPorUsuario.clear();
	}
}
