package gft.dto.etiqueta;

import java.util.ArrayList;
import java.util.List;

import gft.entities.Etiqueta;
import gft.entities.Usuario;

public class EtiquetaMapper {

	public static Etiqueta fromDTO(Usuario usuario, RegistroEtiquetaDTO dto) {
		List<Usuario> usuarios = new ArrayList<>();
		usuarios.add(usuario);

		return new Etiqueta(null, dto.getEtiqueta(), usuarios);
	}

	public static ConsultaEtiquetaDTO fromEntity(Etiqueta etiqueta) {
		return new ConsultaEtiquetaDTO(etiqueta.getId(), etiqueta.getNome());
	}
}
