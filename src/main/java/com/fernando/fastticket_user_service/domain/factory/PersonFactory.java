package com.fernando.fastticket_user_service.domain.factory;

import com.fernando.fastticket_user_service.domain.models.Person;
import com.fernando.fastticket_user_service.domain.models.Rol;
import com.fernando.fastticket_user_service.domain.models.User;

import java.util.Map;
import java.util.Set;

public class PersonFactory {
   public static Person create(Map<String,Object> data){
       Long id=(Long) data.getOrDefault("id",0L);
       String email = (String) data.getOrDefault("email","");
       String password = (String) data.getOrDefault("password", "default");
       boolean emailConfirm = (boolean) data.getOrDefault("confirmEmail", false);
       Set<Rol> roles = (Set<Rol>) data.getOrDefault("roles",Set.of(Rol.builder().code("N/A").build()));
       String name = (String) data.getOrDefault("name","N/A");
       String lastName = (String) data.getOrDefault("lastName","N/A");
       String sex = (String) data.getOrDefault("sex","N/A");
       return new User( id,email, password,roles,name, lastName, sex ,emailConfirm);
   }
}
