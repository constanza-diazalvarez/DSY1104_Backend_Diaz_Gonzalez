package com.milsabores.controller;

import com.milsabores.dto.ActualizarUsuarioDTO;
import com.milsabores.dto.LoginRequestDTO;
import com.milsabores.dto.LoginResponseDTO;
import com.milsabores.dto.RegistroUsuarioDTO;
import com.milsabores.model.Usuario;
import com.milsabores.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios: registro, login y actualización de perfil")
public class UsuariosController {

    private final UsuarioService usuarioService;

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un usuario nuevo en el sistema",
            requestBody = @RequestBody(
                    required = true,
                    description = "Datos del usuario a registrar",
                    content = @Content(schema = @Schema(implementation = RegistroUsuarioDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "409", description = "El usuario ya existe")
            }
    )
    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@org.springframework.web.bind.annotation.RequestBody RegistroUsuarioDTO req) {
        usuarioService.register(req);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Permite al usuario autenticarse y recibir token de acceso",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inicio de sesión exitoso",
                            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@org.springframework.web.bind.annotation.RequestBody LoginRequestDTO req) {
        return ResponseEntity.ok(usuarioService.login(req));
    }

    @Operation(
            summary = "Obtener perfil del usuario",
            description = "Devuelve la información del perfil según email",
            parameters = {
                    @Parameter(name = "email", description = "Email del usuario a consultar", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @GetMapping("/perfil/{email}")
    public ResponseEntity<?> perfil(@PathVariable String email) {
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(usuarioService.buildProfile(usuario));
    }

    @Operation(
            summary = "Actualizar perfil del usuario",
            description = "Permite actualizar parcialmente los datos del perfil",
            parameters = {
                    @Parameter(name = "email", description = "Email del usuario", required = true)
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ActualizarUsuarioDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @PatchMapping("/actualizar/{email}")
    public ResponseEntity<?> actualizarPerfil(
            @PathVariable String email,
            @org.springframework.web.bind.annotation.RequestBody ActualizarUsuarioDTO dto) {

        Usuario u = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Usuario actualizado = usuarioService.patchUpdate(u.getId(), dto);
        var perfil = usuarioService.buildProfile(actualizado);

        return ResponseEntity.ok(perfil);
    }

}
