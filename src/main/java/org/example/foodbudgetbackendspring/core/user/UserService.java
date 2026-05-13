package org.example.foodbudgetbackendspring.core.user;

import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.core.user.dto.TwoFactorAuthUpdateRequest;
import org.example.foodbudgetbackendspring.core.user.model.User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void toggle2FA(User user, TwoFactorAuthUpdateRequest request) {
        user.setRequires2FA(request.twoFactorAuthEnabled());
        userRepository.save(user);
    }
}
