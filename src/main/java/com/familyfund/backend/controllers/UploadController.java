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
    public ResponseEntity<UsuarioDto> upload(@RequestParam("file") MultipartFile file, Principal principal) {
        try {
            // Subimos a Cloudinary en carpeta Familyfund
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "Familyfund"));

            String url = (String) uploadResult.get("secure_url");
            System.out.println("Cloudinary URL: " + url);

            // Buscamos el usuario autenticado
            Usuario userToUpdate = usuarioRepository.findByEmail(principal.getName());
            if (userToUpdate == null) {
                System.err.println("Usuario no encontrado: " + principal.getName());
                return ResponseEntity.notFound().build();
            }

            // Guardamos la nueva URL usando el servicio (incluye lógica de foto por defecto)
            userToUpdate.setPhotoUrl(url);
            Usuario updatedUser = usuarioService.save(userToUpdate);

            // Convertimos a DTO
            UsuarioDto usuarioDto = usuarioService.toDto(updatedUser);

            // Devolvemos al frontend
            return ResponseEntity.ok(usuarioDto);

        } catch (Exception e) {
            System.err.println("Error en upload-photo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

}
