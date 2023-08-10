package com.shixun7zu.controller;

import com.shixun7zu.entity.res.ResponseResult;
import com.shixun7zu.service.AuthorizeService;
import com.shixun7zu.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 校验Controller
 */
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String EMAIL_REGEX = "^[A-Za-z0-9-._]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$";
    private final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{2,16}$";

    @Resource
    private AuthorizeService authorizeService;

    @Resource
    private UserService userService;

    @PostMapping("/valid-email")
    public ResponseResult<?> validateEmail(@Pattern(regexp = EMAIL_REGEX)
                                           @RequestParam("email") String email,
                                           HttpSession session) {
        return authorizeService.sendValidateEmail(email, session.getId());
    }

    @PostMapping("/register")
    public ResponseResult<?> addAccount(@Pattern(regexp = USERNAME_REGEX)
                                        @Length(min = 2, max = 8)
                                        @RequestParam("username") String username,
                                        @Pattern(regexp = EMAIL_REGEX)
                                        @RequestParam("email") String email,
                                        @Length(min = 6,max = 18)
                                        @RequestParam("password") String password,
                                        @Length(min = 6, max = 6)
                                        @RequestParam("code") String code,
                                        HttpSession session) {
        return authorizeService.addAccount(username, email, password, code, session.getId());
    }

    @PostMapping("/find-password")
    public ResponseResult<?> findPassword(@Pattern(regexp = EMAIL_REGEX)
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("code") String code,
                                          HttpSession session) {
        return authorizeService.findPassword(email, password, code, session.getId());
    }


    @GetMapping("/test")
    public String test() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "this is test page!"+user.getUsername();
    }
    @GetMapping("/get-avatar")
    @ResponseBody
    public ResponseResult<?> getAvatar(String text){
        return userService.getAvatar(text);
    }
}
