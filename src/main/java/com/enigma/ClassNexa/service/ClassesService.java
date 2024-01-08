package com.enigma.ClassNexa.service;

import com.enigma.ClassNexa.entity.Classes;
import com.enigma.ClassNexa.model.request.UpdateClassesRequest;

public interface ClassesService {
    Classes getClassById (String id);

    Classes updateClasses (UpdateClassesRequest request);
}
