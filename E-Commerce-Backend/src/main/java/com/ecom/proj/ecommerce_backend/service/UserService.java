package com.ecom.proj.ecommerce_backend.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecom.proj.ecommerce_backend.api.model.LoginBody;
import com.ecom.proj.ecommerce_backend.api.model.RegistrationBody;
import com.ecom.proj.ecommerce_backend.exception.EmailFailureException;
import com.ecom.proj.ecommerce_backend.exception.UserAlreadyExistsException;
import com.ecom.proj.ecommerce_backend.exception.UserNotVerifiedException;
import com.ecom.proj.ecommerce_backend.model.LocalUser;
import com.ecom.proj.ecommerce_backend.model.VerificationToken;
import com.ecom.proj.ecommerce_backend.model.dao.LocalUserDAO;
import com.ecom.proj.ecommerce_backend.model.dao.VerificationTokenDAO;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private LocalUserDAO localUserDAO;
	private EncryptionService encryptionService;
	private JWTService jwtService;
	private EmailService emailService;
	private VerificationTokenDAO verificationTokenDAO;

	public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService,
			EmailService emailService, VerificationTokenDAO verificationTokenDAO) {
		this.localUserDAO = localUserDAO;
		this.encryptionService = encryptionService;
		this.jwtService = jwtService;
		this.emailService = emailService;
		this.verificationTokenDAO = verificationTokenDAO;
	}

	public LocalUser registerUser(RegistrationBody registrationBody)
			throws UserAlreadyExistsException, EmailFailureException {
		if (localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
				&& localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException();
		}

		LocalUser user = new LocalUser();
		user.setEmail(registrationBody.getEmail());
		user.setUsername(registrationBody.getUsername());
		user.setFirstName(registrationBody.getFirstName());
		user.setLastName(registrationBody.getLastName());
		user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
		VerificationToken verificationToken = createVerificationToken(user);
		emailService.sendVerificationEmail(verificationToken);
		verificationTokenDAO.save(verificationToken);
		return localUserDAO.save(user);
	}

	private VerificationToken createVerificationToken(LocalUser user) {
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(jwtService.generateVerificationJWT(user));
		verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
		verificationToken.setUser(user);
		user.getVerificationTokens().add(verificationToken);
		return verificationToken;
	}

	public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
		Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
		if (opUser.isPresent()) {
			LocalUser user = opUser.get();
			if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
				if (user.isEmailVerified()) {
					return jwtService.generateJWT(user);
				} else {
					List<VerificationToken> verificationTokens = user.getVerificationTokens();
					boolean resend = verificationTokens.size() == 0 || verificationTokens.get(0).getCreatedTimestamp()
							.before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
					if (resend) {
						VerificationToken verificationToken = createVerificationToken(user);
						verificationTokenDAO.save(verificationToken);
						emailService.sendVerificationEmail(verificationToken);
					}
					throw new UserNotVerifiedException(resend);
				}
			}
		}
		return null;
	}

	@Transactional
	public boolean verifyUser(String token) {
		Optional<VerificationToken> opToken = verificationTokenDAO.findByToken(token);
		if (opToken.isPresent()) {
			VerificationToken verificationToken = opToken.get();
			LocalUser user = verificationToken.getUser();
			if (!user.isEmailVerified()) {
				user.setEmailVerified(true);
				localUserDAO.save(user);
				verificationTokenDAO.deleteByUser(user);
				return true;
			}
		}
		return false;
	}

}
