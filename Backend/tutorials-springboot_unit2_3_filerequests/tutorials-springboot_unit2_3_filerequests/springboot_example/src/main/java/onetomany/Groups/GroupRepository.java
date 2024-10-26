package onetomany.Groups;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<UserGroup, Long> {
    UserGroup findByName(String name);
}
