package com.milsabores.controller;

import com.milsabores.dto.LoginRequestDTO;
import com.milsabores.dto.LoginResponseDTO;
import com.milsabores.dto.RegistroUsuarioDTO;
import com.milsabores.model.Usuario;
import com.milsabores.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UsuariosController {

    private final UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody RegistroUsuarioDTO req) {
        usuarioService.register(req);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO req) {
        return ResponseEntity.ok(usuarioService.login(req));
    }

    @GetMapping("/perfil/{email}")
    public ResponseEntity<?> perfil(@PathVariable String email) {
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(usuarioService.buildProfile(usuario));
    }
}
