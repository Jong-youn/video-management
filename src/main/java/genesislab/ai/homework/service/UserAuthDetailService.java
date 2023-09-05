package genesislab.ai.homework.service;

import genesislab.ai.homework.repository.UserRepository;
import genesislab.ai.homework.repository.entity.User;
import genesislab.ai.homework.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthDetailService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = repository.findById(Long.valueOf(userId)).orElseThrow(
                () -> new UsernameNotFoundException(String.valueOf(userId))
        );
        return new CustomUserDetails(user);
    }

    public User authenticateByEmailAndPassword(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("로그인 정보를 확인해주시기 바립니다.");
        }

        return user;
    }
}
