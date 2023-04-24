package gft.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gft.entities.HistoricoParametros;

@Repository
public interface HistoricoParametrosRepository extends JpaRepository<HistoricoParametros, Long> {
	List<HistoricoParametros> findByData(String data);
	List<HistoricoParametros> findByIdUsuarioAndData(Long idUsuario, String data);
}
