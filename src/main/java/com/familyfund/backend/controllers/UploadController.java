package com.familyfund.backend.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.familyfund.backend.dto.UsuarioDto;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.UsuarioRepository;
import com.familyfund.backend.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    UsuarioService usuarioService;

    // @SuppressWarnings("unchecked")
    @PostMapping(path = "/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioDto> upload(@RequestParam("file") MultipartFile file, Principal principal)
            throws IOException {

        // Subimos a Cloudinary
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        System.out.println("Respuesta Cloudinary: " +uploadResult); // Respuesta de Cloudinary

        // Obtenemos URL segura
        String url = (String) uploadResult.get("secure_url");

        // Buscamos al usuario autenticado
        Usuario userToUpdate = usuarioRepository.findByEmail(principal.getName());
        if (userToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        // Guardamos nueva URL
        userToUpdate.setPhotoUrl(url);
        Usuario updatedUser = usuarioRepository.save(userToUpdate);

        // Convertimos a DTO
        UsuarioDto usuarioDto = usuarioService.toDto(updatedUser);

        // Devolvemos al frontend
        return ResponseEntity.ok(usuarioDto);
    }
}
