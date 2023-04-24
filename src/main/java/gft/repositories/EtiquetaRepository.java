package gft.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gft.entities.Etiqueta;
import gft.entities.Usuario;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {
	Etiqueta findByNome(String nome);
	Collection<Etiqueta> findByUsuariosIn(List<Usuario> usuarios);
	Collection<Etiqueta> findByNomeAndUsuariosIn(String nomeNovaEtiqueta, List<Usuario> idsNovosUsuarios);
}
