package net.expenses.recorder.repository;

import net.expenses.recorder.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Kazi Tanvir Azad
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u WHERE u.email ILIKE :email")
    Optional<User> getReferenceByEmail(@Param(value = "email") String email);

    @Query(value = "SELECT u FROM User u WHERE u.phone ILIKE :phone")
    Optional<User> getReferenceByPhone(@Param(value = "phone") String phone);

    @Query(value = "SELECT u FROM User u WHERE u.email ILIKE :email OR u.phone ILIKE :phone")
    Optional<User> getReferenceByEmailOrPhone(@Param(value = "email") String email, @Param(value = "phone") String phone);
}
