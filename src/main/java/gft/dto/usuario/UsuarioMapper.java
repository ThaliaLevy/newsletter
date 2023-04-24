package gft.dto.usuario;

import gft.entities.Perfil;
import gft.entities.Usuario;

public class UsuarioMapper {

	public static Usuario fromDTO(RegistroUsuarioDTO dto) {
		Perfil perfil = new Perfil();
		perfil.setId(dto.getPerfilId());			
		
		return new Usuario(null, dto.getNome(), dto.getEmail(), dto.getSenha(), perfil);
	}
	
	public static ConsultaUsuarioDTO fromEntity(Usuario usuario) {
		return new ConsultaUsuarioDTO(usuario.getNome(), usuario.getEmail(), usuario.getPerfil().getNome());
	}
}
