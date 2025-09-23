package com.familyfund.backend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familyfund.backend.dto.JwtResponseDto;
import com.familyfund.backend.dto.LoginDto;
import com.familyfund.backend.dto.MessageResponse;
import com.familyfund.backend.dto.SignupDto;
import com.familyfund.backend.dto.UsuarioDto;
import com.familyfund.backend.modelo.Rol;
import com.familyfund.backend.modelo.Usuario;
import com.familyfund.backend.repositories.UsuarioRepository;
import com.familyfund.backend.security.JwtUtils;
import com.familyfund.backend.security.UserDetailsImpl;

import jakarta.validation.Valid;

//Para manejar CORS
// @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    //LOGING
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String rol = userDetails.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("ERROR");

        return ResponseEntity.<JwtResponseDto>ok(new JwtResponseDto(jwt, "Bearer",
                userDetails.getId(),
                userDetails.getNombre(), 
                userDetails.getEmail(), 
                userDetails.getFamily(),    
                rol));
    }

    //REGISTRO
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDto signUpRequest) {
        if (usuarioRepository.existsByNombre(signUpRequest.getNombre())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ya existe un usuario con ese nombre"));
        }

        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ya existe un usuario con ese email"));
        }

        Rol rolUsuario;
        try {
            rolUsuario = (signUpRequest.getRol() != null)
                    ? Rol.valueOf(signUpRequest.getRol().toUpperCase())
                    : Rol.USER;
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Rol inválido"));
        }

        Usuario user = new Usuario();
        user.setNombre(signUpRequest.getNombre());
        user.setApellido(signUpRequest.getApellido());
        user.setEdad(signUpRequest.getEdad());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRol(rolUsuario);
        user.setFamily(null); // explícito, si no pertenece a ninguna familia aún
        usuarioRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado correctamente"));
    }

    //OBTENER DATOS DE UN USUARIO
        @GetMapping("/user/{id}")
    public ResponseEntity<UsuarioDto> getUser(@PathVariable Long id) {
        Optional<Usuario> user = usuarioRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario u = user.get();
        return ResponseEntity.ok(new UsuarioDto(u.getId(), u.getNombre(), u.getEmail(), u.getFamily()));
    }
}
