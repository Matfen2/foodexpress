package com.foodexpress.service;

import com.foodexpress.dto.request.CreateCustomerRequest;
import com.foodexpress.dto.response.CustomerResponse;
import com.foodexpress.exception.BusinessRuleException;
import com.foodexpress.exception.ResourceNotFoundException;
import com.foodexpress.model.entity.Customer;
import com.foodexpress.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Un client avec l'email " + request.getEmail() + " existe déjà");
        }

        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        Customer saved = customerRepository.save(customer);
        log.info("Client créé : {} {}", saved.getFirstName(), saved.getLastName());

        return toResponse(saved);
    }

    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client #" + id + " introuvable"));
        return toResponse(customer);
    }

    private CustomerResponse toResponse(Customer c) {
        CustomerResponse response = new CustomerResponse();
        response.setId(c.getId());
        response.setFirstName(c.getFirstName());
        response.setLastName(c.getLastName());
        response.setEmail(c.getEmail());
        response.setPhone(c.getPhone());
        response.setAddress(c.getAddress());
        return response;
    }
}