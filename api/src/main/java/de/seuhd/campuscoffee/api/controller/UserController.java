package de.seuhd.campuscoffee.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static de.seuhd.campuscoffee.api.util.ControllerUtils.getLocation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.api.mapper.UserDtoMapper;
import de.seuhd.campuscoffee.domain.ports.UserService;

@Tag(name = "Users", description = "Operations related to user management.")
@Controller
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(
                userService.getAll().stream()
                        .map(userDtoMapper::fromDomain)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userDtoMapper.fromDomain(userService.getById(id))
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<UserDto> getByLoginName(@RequestParam String loginName) {
        return ResponseEntity.ok(
                userDtoMapper.fromDomain(userService.getByLoginName(loginName))
        );
    }

    @PostMapping("")
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        UserDto createdUserDto = userDtoMapper.fromDomain(
                userService.upsert(userDtoMapper.toDomain(userDto))
        );
        return ResponseEntity.created(getLocation(createdUserDto.id())).body(createdUserDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        if (!id.equals(userDto.id())) {
            throw new IllegalArgumentException("User ID in path and body do not match.");
        }
        UserDto updatedUserDto = userDtoMapper.fromDomain(
                userService.upsert(userDtoMapper.toDomain(userDto))
        );
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        userService.delete(id); // throws NotFoundException if no user with the provided ID exists
        return ResponseEntity.noContent().build();
    }
}
