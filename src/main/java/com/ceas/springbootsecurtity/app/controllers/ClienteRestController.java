package com.ceas.springbootsecurtity.app.controllers;


import com.ceas.springbootsecurtity.app.models.entity.Cliente;
import com.ceas.springbootsecurtity.app.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("api/clientes")
public class ClienteRestController {
    @Autowired
    private IClienteService clienteService;

    @GetMapping(value="/clientes")
    public Cliente listarRest(){
        return (Cliente) clienteService.finAll();
    }

}
