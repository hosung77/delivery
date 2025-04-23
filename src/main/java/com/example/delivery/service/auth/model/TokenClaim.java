package com.example.delivery.service.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Getter
@AllArgsConstructor
public class TokenClaim {
	private Long subject;
	private String email;
	private String nickname;
	private List<String> roles;
}
