package siisquad.rutinas.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtTokenUtil; //Se usa para manejar tokens.


    /**
     * Se ejecuta en cada solicitud HTTP.
     * Se encarga de extraer el token de la solicitud,validadarlo y establecer autenticación.
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        //Se obtiene la cabecera "Authorization" de la solicitud.
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.info("No puedo obtener el JWT");
            } catch (ExpiredJwtException e) {
                logger.info("El token ha expirado");
            }
            logger.info("usuario = " + username);
        } else {
            logger.info("El token no comienza con Bearer");
        }

        //Si el nombre de usuario no es nulo y no hay una autenticación existente en el contexto de seguridad, se procede a validar el token.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { //SecurityContextHolder almacena los detalles de quien se ha autenticado
            UserDetails userDetails = new User(username, "", Collections.EMPTY_LIST);

            if (!jwtTokenUtil.isTokenExpired(jwtToken)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                logger.debug("usernamePasswordAuthenticationToken = " + usernamePasswordAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                logger.debug("Token no válido");
            }

        }
        // A la ida

        chain.doFilter(request, response);

        // A la vuelta
    }

}
