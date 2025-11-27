package com.milsabores.service;

import com.milsabores.dto.ActualizarUsuarioDTO;
import com.milsabores.dto.LoginRequestDTO;
import com.milsabores.dto.LoginResponseDTO;
import com.milsabores.dto.RegistroUsuarioDTO;
import com.milsabores.model.Usuario;
import com.milsabores.repository.UsuarioRepository;
import com.milsabores.security.JwtService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /*
    public UsuarioService(UsuarioRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }*/

    public void register(RegistroUsuarioDTO req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email ya registrado");
        }

        LocalDate fecha = null;
        try {
            fecha = LocalDate.parse(req.getFechaNacimiento()); // yyyy-MM-dd esperado
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato de fecha inválido (yyyy-MM-dd)");
        }

        Usuario u = Usuario.builder()
                .nombre(req.getNombre())
                .apellidos(req.getApellidos())
                .email(req.getEmail())
                .fechaNacimiento(fecha)                      // <-- LocalDate aquí
                .password(passwordEncoder.encode(req.getPassword()))
                .codigoPromo(req.getCodigoPromo())
                .build();

        userRepository.save(u);
    }

    public LoginResponseDTO login(LoginRequestDTO req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        Usuario u = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String token = jwtService.generateToken(u.getEmail());
        PerfilUsuario perfil = buildProfile(u);

        return new LoginResponseDTO(
                token,
                perfil.flag50(),
                perfil.flag10(),
                perfil.flagCumple()
        );
    }

    public Optional<Usuario> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Usuario patchUpdate(Long id, ActualizarUsuarioDTO dto) {
        Usuario u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (dto.getNombre() != null) u.setNombre(dto.getNombre());
        if (dto.getApellidos() != null) u.setApellidos(dto.getApellidos());
        if (dto.getFechaNacimiento() != null) {
            u.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getCodigoPromo() != null) {
            u.setCodigoPromo(dto.getCodigoPromo());
        }

        return userRepository.save(u);
    }

    public PerfilUsuario buildProfile(Usuario u) {
        LocalDate hoy = LocalDate.now();
        LocalDate nacimiento = u.getFechaNacimiento();
        int edad = nacimiento == null ? 0 :
                hoy.getYear() - nacimiento.getYear() - ((hoy.getDayOfYear() < nacimiento.getDayOfYear()) ? 1 : 0);

        boolean flag50 = edad >= 50;
        boolean flag10 = "FELICES50".equalsIgnoreCase(u.getCodigoPromo());
        boolean flagCumple = (u.getEmail() != null && u.getEmail().contains("duoc")) &&
                nacimiento != null &&
                nacimiento.getDayOfMonth() == hoy.getDayOfMonth() &&
                nacimiento.getMonth() == hoy.getMonth();

        return new PerfilUsuario(u.getId(), u.getNombre(), u.getApellidos(), u.getEmail(),
                nacimiento, edad, flag50, flag10, flagCumple);
    }

    public static record PerfilUsuario(Long id, String nombre, String apellidos,
                                       String email, LocalDate fechaNacimiento, int edad,
                                       boolean flag50, boolean flag10, boolean flagCumple) {}
}
