package br.com.fiap.skill_hub.service;

import br.com.fiap.skill_hub.controller.dto.LoginDto;
import br.com.fiap.skill_hub.controller.dto.RegisterRequest;
import br.com.fiap.skill_hub.repository.UserRepository;
import br.com.fiap.skill_hub.repository.entities.UserEntity;
import br.com.fiap.skill_hub.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // Cadastro de novo usuário
    public String register(RegisterRequest request) {

        // 1. Verifica se email já existe
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado: " + request.email());
        }

        // 2. Cria a entidade com senha criptografada
        UserEntity user = new UserEntity();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setProfile(request.profile());

        // 3. Salva no banco
        userRepository.save(user);

        // 4. Gera e retorna o token JWT
        return jwtService.generateToken(user);
    }

    // Login do usuário
    public String login(LoginDto request) {

        // 1. Autentica email e senha — lança exceção se inválido
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // 2. Busca o usuário no banco
        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 3. Gera e retorna o token JWT
        return jwtService.generateToken(user);
    }
}