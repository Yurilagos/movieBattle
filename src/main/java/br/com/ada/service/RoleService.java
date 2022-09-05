package br.com.ada.service;

import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ada.domain.Role;
import br.com.ada.repository.RoleRepository;

@Service
@AllArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;

	public Role createOrUpdateRole(Role role) {
		return roleRepository.save(role);
	}

	public Optional<Role> findPlayerRole() {
		return roleRepository.findByRoleName(Role.ROLE_PLAYER);
	}

}