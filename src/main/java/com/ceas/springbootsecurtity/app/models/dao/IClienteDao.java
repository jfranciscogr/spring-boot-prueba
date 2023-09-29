package com.ceas.springbootsecurtity.app.models.dao;

import com.ceas.springbootsecurtity.app.models.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IClienteDao extends JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
    public Cliente fetchByIdWithFacturas(Long id);
}
