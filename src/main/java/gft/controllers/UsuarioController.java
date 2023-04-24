package gft.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gft.dto.etiqueta.EtiquetaMapper;
import gft.dto.etiqueta.RegistroEtiquetaDTO;
import gft.dto.usuario.ConsultaUsuarioDTO;
import gft.dto.usuario.RegistroEtiquetasDoUsuarioDTO;
import gft.dto.usuario.RegistroUsuarioDTO;
import gft.dto.usuario.UsuarioMapper;
import gft.entities.HistoricoParametros;
import gft.entities.ListaNoticias;
import gft.entities.Usuario;
import gft.exceptions.BadRequestException;
import gft.exceptions.ForbiddenException;
import gft.exceptions.InternalServerErrorException;
import gft.services.EnvioEmailService;
import gft.services.EtiquetaService;
import gft.services.UsuarioService;

@RestController
@RequestMapping("v1/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;
	private final EtiquetaService etiquetaService;
	private final EnvioEmailService envioEmailService;

	public UsuarioController(UsuarioService usuarioService, EtiquetaService etiquetaService, EnvioEmailService envioEmailService) {
		this.usuarioService = usuarioService;
		this.etiquetaService = etiquetaService;
		this.envioEmailService = envioEmailService;
	}
	
	public Usuario verificarUsuarioAutenticado() {
		return (Usuario) SecurityContextHolder.getContext()
											  .getAuthentication()
											  .getPrincipal();
	}


	@PostMapping("/cadastrar")
	public ResponseEntity<?> salvarUsuario(@RequestBody RegistroUsuarioDTO dto) {
		Usuario usuarioAutenticado = verificarUsuarioAutenticado();

		if (usuarioAutenticado.getPerfil().getNome().equals("Admin")) {
				usuarioService.salvarUsuario(UsuarioMapper.fromDTO(dto));
				
				enviarEmailParaUsuario(dto);
				
				return  ResponseEntity.status(HttpStatus.OK).body("Usuário cadastrado com sucesso!");
		}	
		
		throw new ForbiddenException("Cadastro de usuários só pode ser realizado por perfil administrador.");
	}
	
	private void enviarEmailParaUsuario(RegistroUsuarioDTO dto) {
		try {
			String email = dto.getEmail();
			String textoAssunto = "Cadastro realizado com sucesso!";
			String textoCorpoEmail = "Seu cadastro foi realizado com sucesso! \n\nAgora você pode cadastrar etiquetas dos temas de seu interesse e usá-las para"
					+ " consultar as notícias de hoje ou datas anteriores.";
			envioEmailService.enviarEmailParaUsuario(email, textoAssunto, textoCorpoEmail);
		} catch (Exception e) {
			throw new InternalServerErrorException("Houve um erro no envio do e-mail.");
		}
	}

	@PostMapping("/etiquetas/vincular")
	public ResponseEntity<?> salvarEtiqueta(@RequestBody RegistroEtiquetaDTO dto) throws Exception {
		Usuario usuarioAutenticado = verificarUsuarioAutenticado();

		if (usuarioAutenticado.getPerfil().getNome().equals("Usuario")) {
			etiquetaService.salvarEtiqueta(EtiquetaMapper.fromDTO(usuarioAutenticado, dto));
			
			return ResponseEntity.status(HttpStatus.OK).body("Etiqueta vinculada ao usuário com sucesso!");
		}

		throw new ForbiddenException("Cadastro de etiquetas só pode ser realizado por perfil sem administrador.");
	}

	@GetMapping("/listar")
	public List<ConsultaUsuarioDTO> listarUsuariosCadastrados(@RequestParam(required = false) String perfil) {
		Usuario usuarioAutenticado = verificarUsuarioAutenticado();

		if (usuarioAutenticado.getPerfil().getNome().equals("Admin")) {
			List<ConsultaUsuarioDTO> usuarios = new ArrayList<>();

			if (perfil != null) {
				List<Usuario> usuariosPorPerfil = usuarioService.listarUsuariosPorPerfil(converterStringPerfilParaLong(perfil));
				converterListaUsuariosEmDto(usuarios, usuariosPorPerfil);
			} else {
				List<Usuario> todosOsUsuarios = usuarioService.listarTodosOsUsuarios();
				converterListaUsuariosEmDto(usuarios, todosOsUsuarios);
			}

			return usuarios;
		}

		throw new ForbiddenException("Consulta de usuários só pode ser realizada por perfil administrador.");
	}

	private List<ConsultaUsuarioDTO> converterListaUsuariosEmDto(List<ConsultaUsuarioDTO> usuarios, List<Usuario> usuariosDaBuscaNoBanco) {
		for (Usuario usuario : usuariosDaBuscaNoBanco) {
			usuarios.add(UsuarioMapper.fromEntity(usuario));
		}
		
		return usuarios;
	}

	private long converterStringPerfilParaLong(String perfil) {
		if (perfil.equalsIgnoreCase("Admin")) {
			perfil = "1";
		} else if (perfil.equalsIgnoreCase("Usuario")) {
			perfil = "2";
		} else {
			throw new BadRequestException("Perfil inexistente.");
		}
		
		return Long.parseLong(perfil);
	}

	@GetMapping("/noticias/antigas")
	public ResponseEntity<?> obterRespostaApiNoticiasAntigas(@RequestParam String q, @RequestParam String date) {
		Usuario usuarioAutenticado = verificarUsuarioAutenticado();
		
		if (usuarioAutenticado.getPerfil().getNome().equals("Usuario")) {
			ListaNoticias noticias = etiquetaService.webClient(usuarioAutenticado, q, date);
			
			return ResponseEntity.ok(noticias);
		}
		
		throw new ForbiddenException("Consulta de notícias só pode ser realizada por perfil sem administrador.");
	}

	@GetMapping("/noticias/hoje")
	public ResponseEntity<?> obterRespostaApiNoticiasHoje(@RequestParam String q) throws ParseException {
		Usuario usuarioAutenticado = verificarUsuarioAutenticado();
		
		if (usuarioAutenticado.getPerfil().getNome().equals("Usuario")) {
			ListaNoticias noticias = etiquetaService.webClient(usuarioAutenticado, q, verificarDataDeHoje());

			return ResponseEntity.ok(noticias);
		}
		
		throw new ForbiddenException("Consulta de notícias só pode ser realizada por perfil sem administrador.");
	}

	private String verificarDataDeHoje() {
		Calendar c = Calendar.getInstance();
		Date data = c.getTime();
		DateFormat f = DateFormat.getDateInstance(DateFormat.DATE_FIELD);

		return f.format(data);
	}

	@GetMapping("/etiquetas-mais-acessadas")
	public ResponseEntity<?> visualizarEtiquetasMaisAcessadas() {
		Usuario usuarioAutenticado = verificarUsuarioAutenticado();

		if (usuarioAutenticado.getPerfil().getNome().equals("Admin")) {
			return ResponseEntity.ok(etiquetaService.listarEtiquetasMaisAcessadas());
		}

		throw new ForbiddenException("Histórico de etiquetas mais acessadas só pode ser visualizado por perfil administrador.");
	}

	@GetMapping("/parametros-acessados")
	public List<HistoricoParametros> visualizarParametrosAcessadosHoje() {
		Usuario usuarioAutenticado = verificarUsuarioAutenticado();

		List<HistoricoParametros> ocorrenciasDoIdDoUsuario = etiquetaService.visualizarParametrosAcessadosHoje(usuarioAutenticado, verificarDataDeHoje());
		
		return ocorrenciasDoIdDoUsuario;
	}

	@RequestMapping(path = "/email-send", method = RequestMethod.GET)
	public ResponseEntity<String> enviarEmail() {
		Usuario usuarioAutenticado = verificarUsuarioAutenticado();

		if (usuarioAutenticado.getPerfil().getNome().equals("Admin")) {
			List<RegistroEtiquetasDoUsuarioDTO> registros = usuarioService.buscarEtiquetasVinculadasAUsuarios();
			
			envioEmailService.enviarEmailDeNoticias(registros, verificarDataDeHoje());
			
			return ResponseEntity.status(HttpStatus.OK).body("E-mail de notícias enviado com sucesso!");
		}
		
		throw new ForbiddenException("Envio de e-mails de notícias só pode ser realizado por perfil administrador."); 
	}
}
