package dio.edu.jwt_spring_project.controller;


import dio.edu.jwt_spring_project.dtos.CreateUserRequestDto;
import dio.edu.jwt_spring_project.dtos.UpdateUserRequestDto;
import dio.edu.jwt_spring_project.dtos.UserResponseDto;
import dio.edu.jwt_spring_project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @Operation(summary = "Retorna todos os usuário", description = "Este endpoint retorna a lista de todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> ObterUsuarios() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Retorna um usuário pelo ID", description = "Este endpoint retorna um único usuário, realizando a busca pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário retornada com sucesso")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> obterClientePorId(
            @Parameter(description = "ID do registro de usuário a ser retornado", required = true, example = "1")
            @PathVariable Long id
    ) {
        Optional<UserResponseDto> user = Optional.ofNullable(userService.getUserById(id));
        if(user.isEmpty()){
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Retorna um usuário pelo username", description = "Este endpoint retorna um único usuário, realizando a busca pelo username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário retornada com sucesso")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<Object> obterUsuarioPeloUsername(
            @Parameter(description = "Username do usuário a ser retornado", required = true, example = "guest")
            @PathVariable String username
    ) {
        Optional<UserResponseDto> user = Optional.ofNullable(userService.getUserByUsername(username));
        if(user.isEmpty()){
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Adiciona um novo usuário", description = "Este endpoint adiciona um novo usuário à base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<String> adicionarUsuario(
            @Parameter(description = "JSON contendo os dados do usuário a ser cadastrado",
                    required = true,
                    example = "{" +
                            "'nome':'João da Silva'," +
                            "'username':'joaosilva'," +
                            "'password':'senha123'," +
                            "'roles':['ROLE_USERS'', 'ROLE_MANAGERS']"+
                            "}"
            )
            @Valid
            @RequestBody CreateUserRequestDto data
    ) {

        userService.addUser(data);
        return new ResponseEntity<>("Usuário adicionado com sucesso", HttpStatus.CREATED);
    }

    @Operation(summary = "Altera os dados de um cliente", description = "Este endpoint altera os dados um cliente previamente cadastrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente editado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> editarUsuarioPorId(
            @Parameter(description = "ID do registro de usuário a ser alterado", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "JSON contendo os dados do usuário a ser alterado",
                    required = true,
                    example = "{" +
                            "'nome':'João da Silva'," +
                            "'username':'jojoaosilva'," +
                            "'password':'senha123'," +
                            "'roles':['ROLE_USERS'', 'ROLE_MANAGERS']"+
                            "}"
            )
            @Valid
            @RequestBody UpdateUserRequestDto data
    ) {
        if(userService.updateUser(id, data).isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userService.updateUser(id, data), HttpStatus.OK);

    }

    @Operation(summary = "Exclui um usuário", description = "Este endpoint deleta os dados de um usuário cadastrado da base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuarioPorId(
            @Parameter(description = "ID do registro de usuário a ser excluído", required = true, example = "1")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("Usuário deletado com sucesso", HttpStatus.NO_CONTENT);
    }


}