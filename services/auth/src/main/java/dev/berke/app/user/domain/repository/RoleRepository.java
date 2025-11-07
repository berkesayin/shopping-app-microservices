package dev.berke.app.user.domain.repository;

import dev.berke.app.user.domain.model.Role;
import dev.berke.app.user.domain.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleType name);
}
