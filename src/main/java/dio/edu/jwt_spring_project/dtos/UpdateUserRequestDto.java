package dio.edu.jwt_spring_project.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateUserRequestDto(String nome,
                                   String username,
                                   List<String> roles,
                                   LocalDateTime dataCriacao,
                                   LocalDateTime dataAlteracao) {
}
