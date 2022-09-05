package br.com.ada.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ada.datamock.DataMock;
import br.com.ada.domain.Player;
import br.com.ada.domain.Role;
import br.com.ada.dto.PlayerDTO;
import br.com.ada.erro.ApiErrorException;
import br.com.ada.repository.PlayerRepository;
import br.com.ada.util.MapperUtil;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

	@InjectMocks
	private PlayerService playerService;

	@Mock
	private PlayerRepository playerRepository;
	@Mock
	private RoleService roleService;
	@Mock
	private MapperUtil mapperUtil;

	private final DataMock dataMock = DataMock.getInstance();

	@DisplayName("should create player with successful")
	@Test
	void shouldCreatePlayerWithSuccessful() {
		var player = dataMock.getPlayer();
		var playerDTO = dataMock.getPlayerDTO();
		var role = dataMock.getRole();

		when(playerRepository.save(any(Player.class))).thenReturn(player);
		when(playerRepository.findByLogin(any(String.class))).thenReturn(Optional.empty());
		when(roleService.findPlayerRole()).thenReturn(Optional.of(role));
		when(mapperUtil.convertTo(playerDTO, Player.class)).thenReturn(player);
		when(roleService.createOrUpdateRole(any(Role.class))).thenReturn(role);
		when(mapperUtil.convertTo(player, PlayerDTO.class)).thenReturn(playerDTO);

		PlayerDTO result = playerService.create(playerDTO, Role.ROLE_PLAYER);

		assertNotNull(result);
		verify(playerRepository, atLeastOnce()).save(any(Player.class));
		verify(roleService, atLeastOnce()).createOrUpdateRole(any(Role.class));

	}
	
	@DisplayName("should throw exception when searching for player that already exist")
	@Test
	void shouldThrowExceptionForPlayerThatAlreadExist() {
		var playerDTO = dataMock.getPlayerDTO();
		var player = dataMock.getPlayer();
		ApiErrorException exception = assertThrows(ApiErrorException.class, () -> {

			when(playerRepository.findByLogin(any(String.class))).thenReturn(Optional.of(player));

			playerService.create(playerDTO, Role.ROLE_PLAYER);
		});
		assertThat(exception.getCode()).isEqualToIgnoringCase("M1006");
	}
}
