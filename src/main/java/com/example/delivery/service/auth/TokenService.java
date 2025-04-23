package com.example.delivery.service.auth;

import com.example.delivery.service.auth.model.Token;
import com.example.delivery.service.auth.model.TokenClaim;


/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
public interface TokenService {
	Token generateToken(TokenClaim tokenClaim);

	TokenClaim parseToken(String token);
}
