package Disney.controller;

import Disney.PostResponse;
import Disney.model.DTO.LoginResponse;
import Disney.model.DTO.UserDTO;
import Disney.model.DTO.UserLoginDTO;
import Disney.model.User;
import Disney.service.EmailService;
import Disney.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import static Disney.utils.Constants.JWT_SECRET;
import static Disney.utils.EntityUrlBuilder.buildURL;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, ModelMapper modelMapper, ObjectMapper objectMapper, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<PostResponse> addUser(@RequestBody User user) throws IOException {
        if(user.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user = userService.addUser(user);
        PostResponse p = new PostResponse(buildURL("api/auth/users", user.getId()), HttpStatus.CREATED);
        emailService.sendEmail("Welcome","Hi",user.getEmail());
        return ResponseEntity.created((p.getUrl())).body(p);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginDTO userDTO) {
        User user = userService.findByEmail(userDTO.getEmail());
        if (user == null || !(passwordEncoder.matches(userDTO.getPassword().trim(), user.getPassword()))){
            throw new BadCredentialsException("Bad user credentials");
        }
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(new LoginResponse(this.generateToken(dto)));
    }

    public String generateToken(UserDTO userDto) {
        try {
            return Jwts
                    .builder()
                    .setId("JWT")
                    .setSubject(userDto.getEmail())
                    .claim("user", objectMapper.writeValueAsString(userDto))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 100000000))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()).compact();
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}