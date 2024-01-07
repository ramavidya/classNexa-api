package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.constan.ERole;
import com.enigma.ClassNexa.entity.Role;


public interface RoleService {
        Role getOrSave(ERole role);
}
