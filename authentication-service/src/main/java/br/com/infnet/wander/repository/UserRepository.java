package br.com.infnet.wander.repository;

import br.com.infnet.wander.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstnameIgnoreCase(String name);

    Optional<User> findByLastnameIgnoreCase(String name);

    Boolean existsByFirstnameIgnoreCase(String username);

    Boolean existsByLastnameIgnoreCase(String username);

    Boolean existsByEmailIgnoreCase(String email);

//    @Query("SELECT u FROM User u WHERE u.status = 1")
//    Collection<User> findAllActiveUsers();
}