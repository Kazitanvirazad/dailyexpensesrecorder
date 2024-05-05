package net.expenses.recorder.repository;

import net.expenses.recorder.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(value = "UPDATE EXPENSE_RECORDER.user SET entrycount = :count WHERE userid = :userId", nativeQuery = true)
    void incrementEntry(@Param(value = "count") int count, @Param(value = "userId") Long userId);

    @Modifying
    @Query(value = "UPDATE EXPENSE_RECORDER.user SET isloggedout = true WHERE userid = :userId", nativeQuery = true)
    void setLoggedOutTrue(@Param(value = "userId") Long userId);

    @Modifying
    @Query(value = "UPDATE EXPENSE_RECORDER.user SET isloggedout = false WHERE userid = :userId", nativeQuery = true)
    void setLoggedOutFalse(@Param(value = "userId") Long userId);

}
