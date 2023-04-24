package gft.dto.usuario;

import java.util.List;

import gft.entities.Etiqueta;

public class RegistroEtiquetasDoUsuarioDTO {

	private String email;
	private List<Etiqueta> etiquetas;
	
	public RegistroEtiquetasDoUsuarioDTO() {}

	public RegistroEtiquetasDoUsuarioDTO(String email, List<Etiqueta> etiquetas) {
		this.email = email;
		this.etiquetas = etiquetas;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Etiqueta> getEtiquetas() {
		return etiquetas;
	}

	public void setEtiquetas(List<Etiqueta> etiquetas) {
		this.etiquetas = etiquetas;
	}
}
