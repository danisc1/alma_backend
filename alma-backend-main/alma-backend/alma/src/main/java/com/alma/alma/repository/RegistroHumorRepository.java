package com.alma.alma.repository;

import com.alma.alma.model.RegistroHumor;
import com.alma.alma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroHumorRepository extends JpaRepository<RegistroHumor, Long> {
    List<RegistroHumor> findByUsuarioOrderByDataDesc(Usuario usuario);
    void deleteByUsuarioId(Long usuarioId);
}
