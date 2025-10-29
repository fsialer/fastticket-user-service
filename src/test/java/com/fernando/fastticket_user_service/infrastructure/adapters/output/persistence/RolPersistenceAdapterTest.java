package com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence;

import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.mappers.RolPersistenceMapper;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.RolEntity;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.repositories.RolRepository;

import com.fernando.fastticket_user_service.utils.TestUtilRol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RolPersistenceAdapterTest {

    @Mock
    private RolRepository rolRepository;

    @Mock
    private RolPersistenceMapper rolPersistenceMapper;

    @InjectMocks
    private RolPersistenceAdapter rolPersistenceAdapter;

    @Test
    @DisplayName("When Find Role By Code Expect Rol Information")
    void When_FindRoleByCode_Expect_RolInformation(){
        RolEntity rolEntity= TestUtilRol.mockRolEntity();
        Rol rol=TestUtilRol.mockRol();
        when(rolRepository.findByCode(anyString())).thenReturn(rolEntity);
        when(rolPersistenceMapper.rolEntityToRol(any())).thenReturn(rol);
        Rol rolResult = rolPersistenceAdapter.findByCode("USER");
        assert(rolResult.equals(rol));
        Mockito.verify(rolRepository,times(1)).findByCode(anyString());
        Mockito.verify(rolPersistenceMapper,times(1)).rolEntityToRol(any());
    }
}
