package dio.edu.jwt_spring_project.service;

import dio.edu.jwt_spring_project.dtos.CreateUserRequestDto;
import dio.edu.jwt_spring_project.dtos.UpdateUserRequestDto;
import dio.edu.jwt_spring_project.dtos.UserResponseDto;
import dio.edu.jwt_spring_project.model.User;
import dio.edu.jwt_spring_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    //@Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(),
                        user.getNome(),
                        user.getUsername(),
                        user.getRoles().stream().toList(),
                        user.getDataCriacao(),
                        user.getDataAlteracao()))
                .toList();
    }

    public UserResponseDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> new UserResponseDto(value.getId(),
                value.getNome(),
                value.getUsername(),
                value.getRoles().stream().toList(),
                value.getDataCriacao(),
                value.getDataAlteracao())).orElse(null);
    }

    public UserResponseDto getUserByUsername(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        return user.map(value -> new UserResponseDto(value.getId(),
                value.getNome(),
                value.getUsername(),
                value.getRoles().stream().toList(),
                value.getDataCriacao(),
                value.getDataAlteracao())).orElse(null);
    }

    public void addUser(CreateUserRequestDto data) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(data.username()));
        if(user.isEmpty()) {
            User newUser = new User(data.username());
            newUser.setNome(data.nome());
            newUser.setPassword(passwordEncoder.encode(data.password()));
            newUser.setRoles(data.roles());
            newUser.setDataCriacao(LocalDateTime.now());
            userRepository.save(newUser);
        }
    }

    public Optional<UserResponseDto> updateUser(Long id, UpdateUserRequestDto data) {
        return userRepository.findById(id).map(u -> {
            u.setNome(data.nome());
            u.setUsername(data.username());
            u.setDataCriacao(u.getDataCriacao());
            u.setDataAlteracao(LocalDateTime.now());

            userRepository.save(u);
            return new UserResponseDto(
                    u.getId(),
                    u.getNome(),
                    u.getUsername(),
                    u.getRoles(),
                    u.getDataCriacao(),
                    u.getDataAlteracao()
            );
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean changePassword (String username, String oldPassword, String newPassword) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        if(user.isPresent()) {
            String encodedOldPassword = user.get().getPassword();
            if(passwordEncoder.encode(oldPassword).equals(encodedOldPassword)) {
                user.get().setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user.get());
                return true;
            }
        }
        return false;
    }

}
