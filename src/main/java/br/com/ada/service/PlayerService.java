package br.com.ada.service;

import br.com.ada.domain.Player;
import br.com.ada.domain.Role;
import br.com.ada.dto.PlayerDTO;
import br.com.ada.erro.ApiErrorException;
import br.com.ada.repository.PlayerRepository;
import br.com.ada.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final RoleService roleService;
    private final MapperUtil mapperUtil;

    public PlayerDTO create(PlayerDTO playerDTO, String roleName) {

        Optional<Player> optionalPlayer = playerRepository.findByLogin(playerDTO.getLogin());

        if (optionalPlayer.isPresent()) {
            throw new ApiErrorException("M1006", HttpStatus.BAD_REQUEST);
        }

        Role role;
        Optional<Role> optionalRole = roleService.findPlayerRole();
        role = optionalRole.orElseGet(() -> roleService.createOrUpdateRole(Role.builder().roleName(roleName).build()));

        Player player = mapperUtil.convertTo(playerDTO, Player.class);
        player.setPassword(new BCryptPasswordEncoder().encode(playerDTO.getPassword()));
        player.setRoles(List.of(role));
        player = playerRepository.save(player);

        role.getPlayers().add(player);
        roleService.createOrUpdateRole(role);
        return mapperUtil.convertTo(player, PlayerDTO.class);
    }


    public Player findPlayerByLogin(String login) {
        return playerRepository.findByLogin(login)
                .orElseThrow(() -> new ApiErrorException("M1005", HttpStatus.BAD_REQUEST));
    }

}
