package br.com.fiap.skill_hub.security;
import br.com.fiap.skill_hub.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthFilter(JwtService jwtService,
                         UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Pega o header Authorization da requisição
        final String authHeader = request.getHeader("Authorization");

        // 2. Se não tem header ou não começa com "Bearer ", deixa passar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrai o token removendo o prefixo "Bearer "
        final String jwt = authHeader.substring(7);

        // 4. Extrai o email do token
        final String email = jwtService.extractUsername(jwt);

        // 5. Se tem email e ainda não está autenticado no contexto
        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Busca o usuário no banco
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            // 7. Valida o token
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. Cria o objeto de autenticação
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 9. Adiciona detalhes da requisição
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // 10. Registra a autenticação no contexto do Spring Security
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // 11. Passa para o próximo filtro
        filterChain.doFilter(request, response);
    }
}