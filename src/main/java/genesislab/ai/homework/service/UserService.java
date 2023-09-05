package genesislab.ai.homework.service;

import genesislab.ai.homework.controller.model.request.UserInfoUpdateReq;
import genesislab.ai.homework.controller.model.request.UserSignupReq;
import genesislab.ai.homework.repository.RoleRepository;
import genesislab.ai.homework.repository.UserRepository;
import genesislab.ai.homework.repository.entity.Role;
import genesislab.ai.homework.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public Long signup(UserSignupReq request) {
        // 이메일 중복여부 확인
        Optional<User> userUsingEmail = repository.findByEmail(request.getEmail());
        if (userUsingEmail.isPresent()) {
            throw new IllegalArgumentException("사용중인 이메일입니다.");
        }

        // password 암호화
        String encryptedPassword = encoder.encode(request.getPassword());
        Optional<Role> role = roleRepository.findById(1L);
        if (role.isEmpty()) {
            log.error("사용자가 회원 레벨을 설정할 수 없습니다.");
            throw new RuntimeException("관리자에게 문의 바랍니다.");
        }

        // repository 저장
        LocalDateTime now = getNowLocalDateTime();
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .password(encryptedPassword)
                .role(role.get())
                .createdAt(now)
                .updatedAt(now)
                .build();
        repository.save(user);
        return user.getId();
    }

    public void updateInfo(UserInfoUpdateReq request) {
        User user = repository.getReferenceById(request.getId());
        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());
        repository.save(user);
    }

    public void delete(Long id) {
        User user = repository.getReferenceById(id);

        LocalDateTime now = getNowLocalDateTime();
        user.setUpdatedAt(now);
        user.setDeletedAt(now);
        repository.save(user);
    }

    private LocalDateTime getNowLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
