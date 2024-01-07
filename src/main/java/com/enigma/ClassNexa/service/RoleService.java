package com.enigma.ClassNexa.service;

import com.enigma.classnexa.constan.ERole;
import com.enigma.classnexa.entity.Role;

public interface RoleService {
        Role getOrSave(ERole role);
}
