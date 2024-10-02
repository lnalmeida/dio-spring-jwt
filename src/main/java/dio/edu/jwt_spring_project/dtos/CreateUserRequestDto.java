package dio.edu.jwt_spring_project.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record CreateUserRequestDto(String nome,
                                   String username,
                                   String password,
                                   List<String> roles,
                                   LocalDateTime dataCriacao,
                                   LocalDateTime dataAlteracao) {
}
