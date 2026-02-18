package com.ead.authuser.services.impl;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserServiceImpl service;

    @Test
    void registerUser() {
        var dto = new UserRecordDto("teste1","Teste Osvaldo","teste1@gmail.com","995647",null,"61981202156","www.teste.com.br/teste.jpg");
        var userCaptor = ArgumentCaptor.forClass(UserModel.class);
        when(repository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        var result = service.registerUser(dto);
        assertNotNull(result);
        verify(repository).save(userCaptor.capture());
        var saved = userCaptor.getValue();
        assertEquals(dto.username(),saved.getUsername());
        assertEquals(dto.fullName(), saved.getFullName());
        assertEquals(dto.email(), saved.getEmail());
        assertEquals(dto.password(), saved.getPassword());
        assertEquals(dto.phoneNumber(), saved.getPhoneNumber());
        assertEquals(dto.imageUrl(), saved.getImageUrl());
        assertEquals(UserStatus.ACTIVE, saved.getUserStatus());
        assertEquals(UserType.USER, saved.getUserType());
        assertNotNull(saved.getCreationDate());
        assertNotNull(saved.getLastUpdateDate());
        assertTrue(saved.getCreationDate().isBefore(LocalDateTime.now(ZoneId.of("UTC")).plusSeconds(1)));
        assertTrue(saved.getLastUpdateDate().isBefore(LocalDateTime.now(ZoneId.of("UTC")).plusSeconds(1)));
        assertSame(saved, result);
    }


    @Test
    void findById() {
        var uuid = UUID.randomUUID();
        var existent = new UserModel();
        existent.setUserId(uuid);
        existent.setFullName("Teste Osvaldo");
        existent.setUsername("teste1");
        existent.setEmail("teste1@gmail.com");
        existent.setPassword("995647");
        existent.setPhoneNumber("61981202156");
        existent.setImageUrl("www.teste.com.br/teste.jpg");
        existent.setUserStatus(UserStatus.ACTIVE);
        existent.setUserType(UserType.USER);
        when(repository.findById(uuid)).thenReturn(Optional.of(existent));
        var result = service.findById(uuid);
        assertTrue(result.isPresent());
        var assertResult = result.get();
        assertEquals(uuid, assertResult.getUserId());
        assertEquals("teste1", assertResult.getUsername());
        assertEquals("Teste Osvaldo", assertResult.getFullName());
        assertEquals("teste1@gmail.com", assertResult.getEmail());
        assertEquals("995647", assertResult.getPassword());
        assertEquals("61981202156", assertResult.getPhoneNumber());
        assertEquals("www.teste.com.br/teste.jpg", assertResult.getImageUrl());
        assertEquals(UserStatus.ACTIVE, assertResult.getUserStatus());
        assertEquals(UserType.USER, assertResult.getUserType());
        verify(repository, times(1)).findById(uuid);
    }
}