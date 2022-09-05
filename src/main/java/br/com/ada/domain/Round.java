package br.com.ada.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import br.com.ada.enumarator.Stats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Round {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "ID_PLAYER")
	private Player player;
	
	private Stats stats;
	
	@ManyToMany
	@JoinTable(name = "REL_ROUND_BALLES", 
	joinColumns = { @JoinColumn(name = "ID_ROUND") }, 
	inverseJoinColumns = {@JoinColumn(name = "ID_BATTLE") }
	)
	private List<Battle> battles;
	
	private Integer errorAmount;

	public void plusOneErrorAmount() {
		if(errorAmount != null) {
			errorAmount++;
		}else {
			errorAmount = 1;
		}
	}

}
