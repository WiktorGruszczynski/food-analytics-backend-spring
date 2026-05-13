package org.example.foodbudgetbackendspring.core.user;

import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.common.dto.SimpleMessageResponse;
import org.example.foodbudgetbackendspring.core.user.dto.TwoFactorAuthUpdateRequest;
import org.example.foodbudgetbackendspring.core.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/me/2fa")
    public ResponseEntity<SimpleMessageResponse> toggle2FA(
            @AuthenticationPrincipal User user,
            @RequestBody TwoFactorAuthUpdateRequest request
            ){
        userService.toggle2FA(user ,request);
        return ResponseEntity.ok(
                new SimpleMessageResponse("2FA updated successfully")
        );
    }
}
