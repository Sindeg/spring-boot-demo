package ru.alishev.firstsecurityapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.firstsecurityapp.model.Person;
import ru.alishev.firstsecurityapp.repository.PersonRepository;

@Service
public class RegistrationService {
    private final PersonRepository personRepository;

    @Autowired
    public RegistrationService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public void register(Person person) {
        personRepository.save(person);
    }
}
