package dev.berke.auth.repository;

import dev.berke.auth.entity.Role;
import dev.berke.auth.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleType name);
}
