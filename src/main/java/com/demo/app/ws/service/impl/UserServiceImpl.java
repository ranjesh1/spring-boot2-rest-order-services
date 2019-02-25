package com.demo.app.ws.service.impl;

import com.demo.app.ws.entities.User;
import com.demo.app.ws.repository.UserRepository;
import com.demo.app.ws.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long id) {

        Optional<User> maybeUser = userRepository.findById(id);
        return maybeUser.orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User createUser(User user) {

        userRepository.findByEmail(user.getEmail())
                .ifPresent(p -> {
                    throw new RuntimeException("User with email " + p.getEmail() + " already exists ");
                });
        return userRepository.save(user);

    }

    @Override
    public User updateUser(Long id, User user) {
        User userFound = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        userFound.setFirstName(user.getFirstName());
        userFound.setLastName(user.getLastName());
        userFound.setEmail(user.getEmail());
        userFound.setFirstLineOfAddress(user.getFirstLineOfAddress());
        userFound.setSecondLineOfAddress(user.getSecondLineOfAddress());
        userFound.setTown(user.getTown());
        userFound.setPostCode(user.getPostCode());

        return userRepository.save(userFound);
    }

    @Override
    public User patchUpdateUser(Long id, User user) {
        User userFound = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        ModelMapper modelMapper = new ModelMapper();
        //copy only non null values
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.map(user, userFound);

        return userRepository.save(userFound);
    }

    @Override
    public void deleteUser(Long id) {
        User userFound = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(userFound);
    }

    @Override
    public List<User> getUsers() {
        Iterable<User> userIterable = userRepository.findAll();
        return StreamSupport.stream(userIterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}
