package com.ceas.springbootsecurtity.app.models.service;


import com.ceas.springbootsecurtity.app.models.entity.Cliente;
import com.ceas.springbootsecurtity.app.models.entity.Factura;
import com.ceas.springbootsecurtity.app.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IClienteService {
    public List<Cliente> finAll();
    public Page<Cliente> finAll(Pageable pageable);
    public void save(Cliente cliente);
    public Cliente findOne(Long id);

    public Cliente fetchByIdWithFacturas(Long id);
    public void delete(long id);
    public List<Producto> findByNombreLikeIgnoreCase(String term);
    public void saveFactura(Factura factura);
    public Producto findProductoById(Long id);
    public Factura findFacturaById(Long id);
    public void deleteFactura(Long id);
    public Factura fetchFacturaByIdWithClienteWhithItemFacturaWithProducto(Long id);

}
