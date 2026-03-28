package br.com.fiap.skill_hub.controller;

import br.com.fiap.skill_hub.api.UserApi;
import br.com.fiap.skill_hub.controller.dto.UserDto;
import br.com.fiap.skill_hub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserApi {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserDto> create(UserDto userDto) {
        UserDto userDtoResponse = userService.create(userDto);
        return ResponseEntity.ok().body(userDtoResponse);
    }

    @Override
    public ResponseEntity<List<UserDto>> list() {
        List<UserDto> allUsers = userService.list();
        return ResponseEntity.ok().body(allUsers);
    }
//
//    @GetMapping(value="/{idUser}", consumes = "application/json")
//    public ResponseEntity<UserDto> findById(@PathVariable Integer idUser) {
//        UserDto userDtoResponse = userService.findById(idUser);
//        return ResponseEntity.ok().body(userDtoResponse);
//    }

//    @DeleteMapping(value="/{idUser}", consumes = "application/json")
//    public ResponseEntity<Void> deleteById(@PathVariable Integer idUser) {
//        userService.deleteById(idUser);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping(value="/{idUser}", consumes = "application/json")
//    public ResponseEntity<Void> update(@PathVariable Integer idUser, @RequestBody UserDto userDto) {
//        userDto.setId(idUser);
//        userService.update(userDto);
//        return  ResponseEntity.noContent().build();
//
//    }

//
//    @PutMapping(value="/{idUser}/skills", consumes = "application/json")
//    public ResponseEntity<UserDto> addSkill(@PathVariable Integer idUser, @RequestBody List<Integer> idSkills) {
//        UserDto userDtoResponse = userService.addSkill(idUser, idSkills);
//        return ResponseEntity.ok().body(userDtoResponse);
//
//    }

}
