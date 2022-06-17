package com.example.springboot;

import com.example.springboot.models.MessageRequest;
import com.example.springboot.models.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping("/api/service")
	public ResponseEntity<MessageResponse> getHashing(@RequestBody MessageRequest jsonStr) {
		System.out.println("#############################################################");
		System.out.println("MessageRequest id: "+ jsonStr.getId());
		System.out.println("MessageRequest List<String>: "+jsonStr.getLines().toString());
		MessageResponse messageResponse = new MessageResponse();
		List<String> outputList = new ArrayList<String>();
		messageResponse.setId(jsonStr.getId());
		for (String temp : jsonStr.getLines()) {
			outputList.add(String.valueOf(temp.hashCode()));
		}
		messageResponse.setLines(outputList);
		System.out.println("MessageResponse id: "+ messageResponse.getId());
		System.out.println("MessageResponse List<String>: "+messageResponse.getLines().toString());
		System.out.println("#############################################################");
		return new ResponseEntity(messageResponse, HttpStatus.OK);
	}

}
