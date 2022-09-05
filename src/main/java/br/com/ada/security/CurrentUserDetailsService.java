package br.com.ada.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.ada.domain.Player;
import br.com.ada.repository.PlayerRepository;


@Component
@Transactional
public class CurrentUserDetailsService implements UserDetailsService {

	@Autowired
	private PlayerRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Player player = userRepository.findByLogin(login)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("Player %d not Found", login)));
		player.getAuthorities().size();

		return new User(player.getUsername(),
				player.getPassword(),
				player.isEnabled(),
				player.isAccountNonExpired(),
				player.isCredentialsNonExpired(),
				player.isAccountNonLocked(),
				player.getAuthorities());
	}

}