package com.example.delivery.service.auth;

import com.example.delivery.service.auth.model.Token;
import com.example.delivery.service.auth.model.TokenClaim;



public interface TokenService {
	Token generateToken(TokenClaim tokenClaim);

	TokenClaim parseToken(String token);
}
