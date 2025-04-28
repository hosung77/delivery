package com.example.delivery.controller.user;
import com.example.delivery.dto.user.req.CreateUserReqDTO;
import com.example.delivery.dto.user.req.UpdateUserReqDTO;
import com.example.delivery.dto.user.res.UpdateUserResDTO;
import com.example.delivery.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<Void> createUser(
            @RequestBody CreateUserReqDTO user
    ) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity<UpdateUserResDTO> updateUser(
            @AuthenticationPrincipal String userIdStr,
            @RequestBody UpdateUserReqDTO user
    ){
        Long userId = Long.parseLong(userIdStr);
        UpdateUserResDTO updateUserResDTO = userService.updateUser(user,userId);
        return ResponseEntity.ok(updateUserResDTO);
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal String userIdStr
    ){
        Long userId = Long.parseLong(userIdStr);
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }




}
