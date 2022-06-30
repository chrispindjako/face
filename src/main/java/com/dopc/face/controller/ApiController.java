package com.dopc.face.controller;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dopc.face.service.ApiService;

import net.minidev.json.parser.ParseException;

/**
 * @author DOPC
 * 
 * Controller to manage API
 */
@CrossOrigin
@RestController
@RequestMapping(value="/api")
public class ApiController {
	
	@Autowired
	private ApiService apiService;

	@GetMapping(value="/{map}")
	public List<Map<String, Object>> expose (@RequestHeader(required=false) String Authorization, @PathVariable String map, @RequestParam(required=false) Map<String,String> requestParams) throws ParseException, SQLException, IOException {
		  
		return apiService.expose(Authorization, map, requestParams);
	}
	
}
