package com.ceas.springbootsecurtity.app.models.service;


import com.ceas.springbootsecurtity.app.models.dao.IClienteDao;
import com.ceas.springbootsecurtity.app.models.dao.IFacturaDao;
import com.ceas.springbootsecurtity.app.models.dao.IProductoDao;
import com.ceas.springbootsecurtity.app.models.entity.Cliente;
import com.ceas.springbootsecurtity.app.models.entity.Factura;
import com.ceas.springbootsecurtity.app.models.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements  IClienteService{
    @Autowired
    private IClienteDao clienteDao;
    @Autowired
    private IFacturaDao facturaDao;
    @Autowired
    private IProductoDao productoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> finAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> finAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public Cliente findOne(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente fetchByIdWithFacturas(Long id) {
        return clienteDao.fetchByIdWithFacturas(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
            clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByNombreLikeIgnoreCase(String term) {
        return productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
    }

    @Override
    @Transactional
    public void saveFactura(Factura factura) {
            facturaDao.save(factura);
    }

    @Override
    @Transactional(readOnly=true)
    public Producto findProductoById(Long id) {
        return productoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly=true)
    public Factura findFacturaById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteFactura(Long id) {
        facturaDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly=true)
    public Factura fetchFacturaByIdWithClienteWhithItemFacturaWithProducto(Long id) {
        return facturaDao.fetchByIdWithClienteWhithItemFacturaWithProducto(id);
    }




}
