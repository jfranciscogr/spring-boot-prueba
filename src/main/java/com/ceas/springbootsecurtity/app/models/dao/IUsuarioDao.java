package com.ceas.springbootsecurtity.app.models.dao;


import com.ceas.springbootsecurtity.app.models.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioDao extends JpaRepository<Usuario, Long> {

    public Usuario findByUsername(String username);
}
