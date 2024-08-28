package com.example.service;

import com.example.exception.EmailAlreadyExistException;
import com.example.exception.RegionNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.grpc.*;
import com.example.model.Region;
import com.example.model.User;
import com.example.repository.RegionRepository;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class GRPCUserService extends UserServerGrpc.UserServerImplBase {

    private final UserService userService;
    private final RegionRepository regionRepository;

    @Override
    public void addUser(GRPCUserRequest request, StreamObserver<Empty> responseObserver) {
        try {
            Region region = regionRepository.findByRegionCode(request.getRegionCode())
                    .orElseThrow(() -> new RegionNotFoundException("Region not found"));

            User user = new User(request, region);

            userService.createUser(user);

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (EmailAlreadyExistException e) {
            responseObserver.onError(Status.ALREADY_EXISTS
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (RegionNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }


    @Override
    public void getUser(UserId request, StreamObserver<GRPCUserResponse> responseObserver) {
        try {
            User user = userService.getUserById(request.getId());

            Region region = user.getRegion();

            GRPCRegion grpcRegion = GRPCRegion.newBuilder()
                    .setRegionName(region.getRegionName())
                    .setRegionCode(region.getRegionCode())
                    .build();

            GRPCUserResponse response = GRPCUserResponse.newBuilder()
                    .setId(user.getId().intValue())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setEmail(user.getEmail())
                    .setRegion(grpcRegion)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (UserNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }


    @Override
    public void deleteUser(UserId request, StreamObserver<Empty> responseObserver) {
        try {
            userService.deleteUser(request.getId());

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();

        } catch (UserNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getUsers(Empty request, StreamObserver<GRPCUsersResponse> responseObserver) {
        List<User> users = userService.getUsers();

        GRPCUsersResponse.Builder responseBuilder = GRPCUsersResponse.newBuilder();

        for (User user : users) {
            Region region = user.getRegion();

            GRPCRegion grpcRegion = GRPCRegion.newBuilder()
                    .setRegionName(region.getRegionName())
                    .setRegionCode(region.getRegionCode())
                    .build();

            GRPCUserResponse userResponse = GRPCUserResponse.newBuilder()
                    .setId(user.getId().intValue())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setEmail(user.getEmail())
                    .setRegion(grpcRegion)
                    .build();
            responseBuilder.addUsers(userResponse);
        }

        GRPCUsersResponse response = responseBuilder.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
