package com.ecom.proj.ecommerce_backend.model.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.ecom.proj.ecommerce_backend.model.LocalUser;
import com.ecom.proj.ecommerce_backend.model.VerificationToken;

import java.util.Optional;

/**
 * Data Access Object for the VerificationToken data.
 */
public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {

  Optional<VerificationToken> findByToken(String token);

  void deleteByUser(LocalUser user);

}
