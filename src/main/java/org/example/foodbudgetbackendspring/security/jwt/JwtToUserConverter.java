package org.example.foodbudgetbackendspring.security.jwt;


import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.core.user.UserRepository;
import org.example.foodbudgetbackendspring.core.user.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JwtToUserConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final UserRepository userRepository;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        User user = userRepository.findByEmail(
                jwt.getClaimAsString("email")
        ).orElseThrow(
                () -> new AccessDeniedException("Unauthorized")
        );

       return new UsernamePasswordAuthenticationToken(
               user,
               null,
               user.getAuthorities()
       );
    }
}
