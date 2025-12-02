package de.seuhd.campuscoffee.tests.system;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.userRequests;

import java.util.List;

import org.junit.Test;

import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;

public class UsersSystemTests extends AbstractSysTest {

   @Test
   void createUser() {
       User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
       User createdUser = userDtoMapper.toDomain(userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst());

       assertEqualsIgnoringIdAndTimestamps(createdUser, userToCreate);
   }

    //TODO: Add at least two additional tests for user operations

}