package com.example.service;

import com.example.grpc.*;
import com.example.web.dto.RegionDto;
import com.example.web.dto.UserDto;
import com.example.web.payload.CreateUserRequest;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @GrpcClient(value = "data-generator-blocking")
    private UserServerGrpc.UserServerBlockingStub blockingStub;

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createUser(CreateUserRequest request) {
        try {
            GRPCUserRequest grpcUserRequest = GRPCUserRequest.newBuilder()
                    .setFirstName(request.getFirstName())
                    .setLastName(request.getLastName())
                    .setEmail(request.getEmail())
                    .setRegionCode(request.getRegionCode())
                    .build();

            blockingStub.addUser(grpcUserRequest);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.NOT_FOUND.getCode()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getStatus().getDescription());
            }
            if (e.getStatus().getCode() == Status.ALREADY_EXISTS.getCode()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getStatus().getDescription());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
            }
        }
    }


    @Override
    public UserDto getUser(Long userId) {
        try {
            UserId request = UserId.newBuilder()
                    .setId(userId)
                    .build();

            GRPCUserResponse response = blockingStub.getUser(request);

            RegionDto regionDto = new RegionDto();
            regionDto.setRegionName(response.getRegion().getRegionName());
            regionDto.setRegionCode(response.getRegion().getRegionCode());

            return UserDto.builder()
                    .userId(response.getId())
                    .firstName(response.getFirstName())
                    .lastName(response.getLastName())
                    .email(response.getEmail())
                    .region(regionDto)
                    .build();

        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.NOT_FOUND.getCode()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getStatus().getDescription());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
            }
        }
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void deleteUser(Long userId) {
        try {
            UserId request = UserId.newBuilder()
                    .setId(userId)
                    .build();

            blockingStub.deleteUser(request);

        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.NOT_FOUND.getCode()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getStatus().getDescription());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
            }
        }
    }

    @Override
    public List<UserDto> getUsers() {
        GRPCUsersResponse response = blockingStub.getUsers(Empty.newBuilder().build());

        return response.getUsersList().stream()
                .map(grpcUser -> UserDto.builder()
                        .userId(grpcUser.getId())
                        .firstName(grpcUser.getFirstName())
                        .lastName(grpcUser.getLastName())
                        .email(grpcUser.getEmail())
                        .region(RegionDto.builder()
                                .regionName(grpcUser.getRegion().getRegionName())
                                .regionCode(grpcUser.getRegion().getRegionCode())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
}
