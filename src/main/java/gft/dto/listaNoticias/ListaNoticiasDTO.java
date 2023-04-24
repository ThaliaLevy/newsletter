package gft.dto.listaNoticias;

import java.util.List;

public class ListaNoticiasDTO {

	private List<RegistroNomeELinkNoticiasDTO> list;

	public ListaNoticiasDTO() {}

	public ListaNoticiasDTO(List<RegistroNomeELinkNoticiasDTO> list) {
		this.list = list;
	}

	public List<RegistroNomeELinkNoticiasDTO> getList() {
		return list;
	}

	public void setList(List<RegistroNomeELinkNoticiasDTO> list) {
		this.list = list;
	}
}
