package backend.academy.apigateway.mapper;


import backend.academy.apigateway.dto.security.UserDto;


import java.util.stream.Collectors;


public class UserMapper {

//    public static UserDto toDto(Users user){
//        return UserDto
//                .builder()
//                .username(user.getUsername())
//                .password(null)
//                .build();
//    }
//
//    public static UserDto toDtoWithFetching(Users user){
//        return UserDto
//                .builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .roles(user.getRoles().stream().map(RoleMapper::toDto).collect(Collectors.toSet()))
//                .build();
//    }
//    public static Users toEntity(UserDto dto){
//        return Users
//                .builder()
//                .id(dto.getId())
//                .username(dto.getUsername())
//                .password(dto.getPassword())
//                .build();
//    }
}
