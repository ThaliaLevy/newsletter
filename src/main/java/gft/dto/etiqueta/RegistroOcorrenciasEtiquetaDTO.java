package gft.dto.etiqueta;

public class RegistroOcorrenciasEtiquetaDTO implements Comparable<RegistroOcorrenciasEtiquetaDTO> {

	private String nomeEtiqueta;
	private Integer quantidadeOcorrencias;

	public RegistroOcorrenciasEtiquetaDTO() {}

	public RegistroOcorrenciasEtiquetaDTO(String nomeEtiqueta, Integer quantidadeOcorrencias) {
		this.nomeEtiqueta = nomeEtiqueta;
		this.quantidadeOcorrencias = quantidadeOcorrencias;
	}

	public String getNomeEtiqueta() {
		return nomeEtiqueta;
	}

	public void setNomeEtiqueta(String nomeEtiqueta) {
		this.nomeEtiqueta = nomeEtiqueta;
	}

	public Integer getQuantidadeOcorrencias() {
		return quantidadeOcorrencias;
	}

	public void setQuantidadeOcorrencias(Integer quantidadeOcorrencias) {
		this.quantidadeOcorrencias = quantidadeOcorrencias;
	}

	@Override
	public int compareTo(RegistroOcorrenciasEtiquetaDTO teste) {
		return quantidadeOcorrencias.compareTo(teste.getQuantidadeOcorrencias());
	}
}
