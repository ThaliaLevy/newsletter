package gft.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_etiquetas")
public class Etiqueta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	
	@ManyToMany
	@JoinTable(name = "tb_usuario_etiquetas", joinColumns={@JoinColumn(name="etiqueta_id")},
											  inverseJoinColumns= {@JoinColumn(name = "usuario_id")})
	private List<Usuario> usuarios;

	public Etiqueta() {}

	public Etiqueta(Long id, String nome, List<Usuario> usuarios) {
		this.id = id;
		this.nome = nome;
		this.usuarios = usuarios;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@Override
	public String toString() {
		return "" + id;
	}
}
