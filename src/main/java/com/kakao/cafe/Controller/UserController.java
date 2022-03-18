package com.kakao.cafe.Controller;

import com.kakao.cafe.Controller.dto.LoginForm;
import com.kakao.cafe.Controller.dto.UserRequestDto;
import com.kakao.cafe.Controller.dto.UserDto;
import com.kakao.cafe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/signup")
    public String signUpForm(Model model) {
        logger.info("[signUpForm] - START");
        model.addAttribute("userRequestDto", new UserRequestDto());

        return "user/form";
    }

    @PostMapping("/users")
    public String createUser(UserRequestDto userRequestDto) {
        logger.info("[createUser] : {}", userRequestDto);
        userService.save(userRequestDto);

        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<UserDto> allUsers = userService.findUsers();

        model.addAttribute("allUsers", allUsers);

        return "/user/list";
    }

    @GetMapping("/users/{userId}")
    public String showUser(@PathVariable String userId, Model model) {
        UserDto findUser = userService.findUserDto(userId);
        logger.info("[showUser] : {}", findUser);

        model.addAttribute("findUser", findUser);

        return "/user/profile";
    }

    @GetMapping("/users/{userId}/form")
    public String updateForm(@PathVariable String userId, Model model) {
        UserRequestDto userRequestDto = userService.findUserRequestDto(userId);
        model.addAttribute("userRequestDto", userRequestDto);
        logger.info("[updateForm] : Update {}", userRequestDto.toString());

        return "user/updateForm";
    }

    @PutMapping("/users/{userId}/update")
    public String updateUser(UserRequestDto userRequestDto) {
        userService.update(userRequestDto);
        logger.info("[updateUser] : END");

        return "redirect:/users";
    }

    @GetMapping("/users/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());

        return "user/login";
    }

    @PostMapping("/users/login")
    public String login(LoginForm loginForm, Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return "user/login";
        }

        if (userService.isLoginSuccess(loginForm)) {
            HttpSession session = request.getSession();
            session.setAttribute("loginInfo", loginForm);

        }

    }

}
