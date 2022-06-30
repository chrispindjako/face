package com.dopc.face.controller;


import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DOPC
 * 
 * Controller to manage API
 */
@CrossOrigin
@RestController
@RequestMapping(value="/token")
public class TokenController {
	
	@GetMapping(value="/generate")
	public String generate () {
		return UUID.randomUUID().toString();
	}
	
}
