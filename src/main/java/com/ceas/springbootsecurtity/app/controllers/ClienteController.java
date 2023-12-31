package com.ceas.springbootsecurtity.app.controllers;


import com.ceas.springbootsecurtity.app.models.entity.Cliente;
import com.ceas.springbootsecurtity.app.models.service.IClienteService;
import com.ceas.springbootsecurtity.app.models.service.IUploadFileService;
import com.ceas.springbootsecurtity.app.util.paginator.PageRender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.net.MalformedURLException;

import java.util.Collection;
import java.util.Map;


@Controller
@SessionAttributes("cliente")
public class ClienteController {
    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    private final Logger log = LoggerFactory.getLogger(getClass());

//    mas de un rol
//    @Secured({"ROLE_USER". "ROLE_ADMIN"})
//    @Secured("ROLE_USER")
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

        Resource recurso = null;

        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

//        Cliente cliente = clienteService.findOne(id);
        Cliente cliente = clienteService.fetchByIdWithFacturas(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Detalle cliente: " + cliente.getNombre());
        return "ver";
    }
//    @Secured("ROLE_USER")
   // @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value= {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, HttpServletRequest request){


        Pageable pageRequest= PageRequest.of(page,4);
        Page<Cliente> clientes= clienteService.finAll(pageRequest);
        PageRender<Cliente> pageRender= new PageRender<>("/listar",clientes);
        model.addAttribute("titulo","Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page",pageRender);
        return "listar";
    }

//    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form")
    public String crear(Map<String,Object> model){
        Cliente cliente= new Cliente();
        model.put("cliente",cliente);
        model.put("titulo","Formulario de Cliente");
        return "form";
    }

//    @Secured("ROLE_ADMIN")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String,Object> model, RedirectAttributes flash){
        Cliente cliente = null;
      if (id>0){
          cliente= clienteService.findOne(id);
            if (cliente==null){
                flash.addFlashAttribute("error", "El  Id del cliente no existe");
            }
      } else {
          flash.addFlashAttribute("error", "El  Id del cliente no existe");
          return "redirect:/listar";
      }
        model.put("cliente",cliente);
        model.put("titulo","Editar de Cliente");
        return "form";

    }

//    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status){
        if (result.hasErrors()){
            model.addAttribute("titulo","Formulario de Cliente");
            return "form";
        }
        if (!foto.isEmpty()){
            if (cliente.getId()!=null && cliente.getId()>0 && cliente.getFoto()!=null && cliente.getFoto().length()>0){
                uploadFileService.delete(cliente.getFoto());
            }
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(foto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            flash.addFlashAttribute("info","Has subido correctamente '" + uniqueFilename +"'");
            cliente.setFoto(uniqueFilename);
        }
        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", "Cliente creado con exito!");
        return "redirect:listar";
    }

//    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash){
        if (id>0){
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito!");


            if (uploadFileService.delete(cliente.getFoto())){
                flash.addFlashAttribute("info", "foto "+ cliente.getFoto() + " eliminada con exito");
            }

        }

        return "redirect:/listar";
    }


}
