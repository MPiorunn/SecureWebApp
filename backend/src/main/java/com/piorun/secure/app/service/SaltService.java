package com.piorun.secure.app.service;

import com.piorun.secure.app.model.Salt;
import com.piorun.secure.app.repository.SaltRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class SaltService {

    private final SaltRepository saltRepository;

    public Optional<Salt> findById(String saltId) {
        return saltRepository.findById(saltId);
    }

    public Salt save(Salt salt) {
        return saltRepository.save(salt);
    }

    public void deleteById(String saltId) {
        saltRepository.deleteById(saltId);
    }
}
