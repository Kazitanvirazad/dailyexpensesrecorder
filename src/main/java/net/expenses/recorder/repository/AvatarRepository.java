package net.expenses.recorder.repository;

import net.expenses.recorder.dao.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Kazi Tanvir Azad
 */
@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {

    @Query(value = "SELECT a.avatarid, a.avatarencoded, a.isdefaultavatar FROM EXPENSE_RECORDER.avatar a WHERE a.isdefaultavatar = true", nativeQuery = true)
    Optional<List<Avatar>> getDefaultAvatar();

    @Query(value = "SELECT a.avatarid, a.avatarencoded, a.isdefaultavatar FROM EXPENSE_RECORDER.avatar a", nativeQuery = true)
    Optional<List<Avatar>> getAvatarList();
}
