package io.my.rsocket.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final PasswordEncoder encoder;

    private Map<String, UserDetails> db;

    @PostConstruct
    private void init() {
        String password = encoder.encode("password");
        this.db = Map.of(
                "user", User.withUsername("user").password(password).roles("USER").build(),
                "admin", User.withUsername("admin").password(password).roles("ADMIN").build(),
                "client", User.withUsername("client").password(password).roles("TRUSTED_CLIENT").build()
        );
    }

    public UserDetails findByUsername(String username) {
        return this.db.get(username);
    }
}
