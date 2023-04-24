package gft.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import gft.dto.etiqueta.RegistroOcorrenciasEtiquetaDTO;
import gft.entities.Etiqueta;
import gft.entities.HistoricoParametros;
import gft.entities.ListaNoticias;
import gft.entities.Usuario;
import gft.exceptions.AlreadyReportedException;
import gft.exceptions.NotFoundException;
import gft.repositories.EtiquetaRepository;
import gft.repositories.HistoricoParametrosRepository;
import reactor.core.publisher.Mono;

@Service
public class EtiquetaService {

	private final EtiquetaRepository etiquetaRepository;
	private final HistoricoParametrosRepository historicoParametrosRepository;
	private final WebClient webClient;

	public EtiquetaService(EtiquetaRepository etiquetaRepository, WebClient webClient, HistoricoParametrosRepository historicoParametrosRepository) {
		this.etiquetaRepository = etiquetaRepository;
		this.historicoParametrosRepository = historicoParametrosRepository;
		this.webClient = webClient;
	}
	
	public Etiqueta salvarEtiqueta(Etiqueta etiqueta) throws Exception {
		Etiqueta etiquetaExistente = buscarEtiquetaPorNome(etiqueta.getNome());

		if(etiquetaExistente == null) {
			etiquetaRepository.save(etiqueta);
			return etiqueta;
		} else {
			if (verificarSeEtiquetaJaExisteParaUsuario(etiqueta.getNome(), etiqueta.getUsuarios()).isEmpty()) {
				atualizarEtiqueta(etiqueta, etiquetaExistente.getId());
				return etiqueta;
			} else {
				throw new AlreadyReportedException("Etiqueta já é vinculada ao usuário.");
			}
		}
	}

	public Collection<Etiqueta> verificarSeEtiquetaJaExisteParaUsuario(String nomeEtiqueta, List<Usuario> usuario) {
		return etiquetaRepository.findByNomeAndUsuariosIn(nomeEtiqueta, usuario);
	}

	public Etiqueta atualizarEtiqueta(Etiqueta etiqueta, Long id) {
		Etiqueta etiquetaOriginal = this.buscarEtiqueta(id);

		List<Usuario> usuarios = new ArrayList<>();
		usuarios.addAll(etiquetaOriginal.getUsuarios());
		usuarios.addAll(etiqueta.getUsuarios());

		etiqueta.setUsuarios(usuarios);
		etiqueta.setId(etiquetaOriginal.getId());

		return etiquetaRepository.save(etiqueta);
	}

	public Etiqueta buscarEtiquetaPorNome(String nome) {
		try {
			Etiqueta optional = etiquetaRepository.findByNome(nome);
		
			return optional;
		} catch (NullPointerException e) {
			return null;
		}
	}

	public Etiqueta buscarEtiqueta(Long id) {
		Optional<Etiqueta> optional = etiquetaRepository.findById(id);

		return optional.orElseThrow(() -> new EntityNotFoundException("Etiqueta não encontrada!"));
	}

	public ListaNoticias webClient(Usuario usuario, String q, String date) {
		List<Usuario> usuarios = new ArrayList<>();
		usuarios.add(usuario);

		Collection<Etiqueta> etiquetaExistente = verificarSeEtiquetaJaExisteParaUsuario(q, usuarios);

		if (!etiquetaExistente.isEmpty()) {
			Mono<ListaNoticias> monoNoticia = this.webClient.method(HttpMethod.GET)
												  .uri(uriBuilder -> uriBuilder
														  .path("api/")
														  .queryParam("q", q)
														  .queryParam("date", date)
														  .build())
												  .retrieve()
												  .bodyToMono(ListaNoticias.class);

			ListaNoticias noticia = monoNoticia.block();

			historicoParametrosRepository.save(new HistoricoParametros(null, q.toLowerCase(), date, usuario.getId()));
			return noticia;
		}
		
		throw new NotFoundException("Etiqueta não encontrada no cadastro de etiquetas vinculadas ao usuário.");
	}
	
	public List<RegistroOcorrenciasEtiquetaDTO> listarEtiquetasMaisAcessadas() {
		List<HistoricoParametros> historicoCompletoDeParametros = historicoParametrosRepository.findAll();

		if(!historicoCompletoDeParametros.isEmpty()) {
			List<String> etiquetas = new ArrayList<>();

			for (HistoricoParametros parametros : historicoCompletoDeParametros) {
				etiquetas.add(parametros.getEtiqueta());
			}

			List<RegistroOcorrenciasEtiquetaDTO> registrosOrdenados = new ArrayList<>();
			Set<String> todasAsEtiquetas = new HashSet<String>(etiquetas);
			
			for (String nomeEtiqueta : todasAsEtiquetas)
				registrosOrdenados.add(new RegistroOcorrenciasEtiquetaDTO(nomeEtiqueta, Collections.frequency(etiquetas, nomeEtiqueta)));		
			
			Collections.sort(registrosOrdenados);
			Collections.reverse(registrosOrdenados);
			
			return registrosOrdenados;
		}
		
		throw new NotFoundException("Não há histórico de etiquetas acessadas.");
	}

	public List<HistoricoParametros> visualizarParametrosAcessadosHoje(Usuario usuarioAutenticado, String data) {
		if (usuarioAutenticado.getPerfil().getNome().equals("Admin")) {
			List<HistoricoParametros> historicoParametrosAcessadosTodosUsuarios = historicoParametrosRepository.findByData(data);
			
			if(historicoParametrosAcessadosTodosUsuarios.isEmpty()) {
				throw new NotFoundException("Não há histórico de parâmetros acessados hoje.");
			}
			
			return historicoParametrosAcessadosTodosUsuarios;
		} else {
			List<HistoricoParametros> ocorrenciasDoIdDoUsuario = historicoParametrosRepository.findByIdUsuarioAndData(usuarioAutenticado.getId(), data);
			if (ocorrenciasDoIdDoUsuario.isEmpty()) {
				throw new NotFoundException("Não há histórico de parâmetros acessados hoje.");
			}
			
			return ocorrenciasDoIdDoUsuario;
		}
	}
}
