package br.com.ada.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Id;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {

    @Id
    private Long id;

    private String title;

    private String year;

    private String director;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Double imdbRating;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Double imdbVotes;

}
