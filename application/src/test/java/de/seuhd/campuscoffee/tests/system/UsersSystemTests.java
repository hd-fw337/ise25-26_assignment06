package de.seuhd.campuscoffee.tests.system;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.userRequests;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;

public class UsersSystemTests extends AbstractSysTest {

    @Test
    void createUser() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        User createdUser = userDtoMapper.toDomain(userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst());
 
        assertEqualsIgnoringIdAndTimestamps(createdUser, userToCreate);
    }

    @Test
    void getAllUsers() {
        List<User> usersToCreate = TestFixtures.getUserListForInsertion();
        userRequests.create(usersToCreate.stream().map(userDtoMapper::fromDomain).toList());

        List<User> retrievedUsers = userRequests.retrieveAll().stream()
                .map(userDtoMapper::toDomain)
                .toList();

        assertEqualsIgnoringFields(retrievedUsers, usersToCreate, "id", "createdAt", "updatedAt");
    }

    @Test
    void updateUser() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        UserDto createdUserDto = userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst();
        User createdUser = userDtoMapper.toDomain(createdUserDto);

        User userToUpdate = createdUser.toBuilder().firstName("UpdatedName").build();
        UserDto updatedUserDto = userRequests.update(List.of(userDtoMapper.fromDomain(userToUpdate))).getFirst();
        User updatedUser = userDtoMapper.toDomain(updatedUserDto);

        assertEqualsIgnoringIdAndTimestamps(updatedUser, userToUpdate);

        // Verify persistence
        UserDto retrievedUserDto = userRequests.retrieveById(updatedUser.id());
        assertEqualsIgnoringIdAndTimestamps(userDtoMapper.toDomain(retrievedUserDto), userToUpdate);
    }

    @Test
    void deleteUser() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        UserDto createdUser = userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst();

        List<Integer> statusCodes = userRequests.deleteAndReturnStatusCodes(List.of(createdUser.id()));

        assertThat(statusCodes).containsExactly(org.springframework.http.HttpStatus.NO_CONTENT.value());

        List<UserDto> allUsers = userRequests.retrieveAll();
        assertThat(allUsers).extracting(UserDto::id).doesNotContain(createdUser.id());
    }

    @Test
    void getUserByLoginName() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate)));

        UserDto retrievedUserDto = userRequests.retrieveByFilter("loginName", userToCreate.loginName());
        User retrievedUser = userDtoMapper.toDomain(retrievedUserDto);

        assertEqualsIgnoringIdAndTimestamps(retrievedUser, userToCreate);
    }

    @Test
    void createDuplicateUser() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate)));

        List<Integer> statusCodes = userRequests.createAndReturnStatusCodes(List.of(userDtoMapper.fromDomain(userToCreate)));
        assertThat(statusCodes).containsExactly(org.springframework.http.HttpStatus.CONFLICT.value());
    }

    @Test
    void updateUserWithIdMismatch() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        UserDto createdUserDto = userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst();

        long wrongId = createdUserDto.id() + 1;

        io.restassured.RestAssured.given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(createdUserDto)
                .when()
                .put("/api/users/{id}", wrongId)
                .then()
                .statusCode(org.springframework.http.HttpStatus.BAD_REQUEST.value());
    }
}
