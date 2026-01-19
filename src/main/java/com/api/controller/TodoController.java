package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.TodoDto;
import com.api.dto.TodoDto;
import com.api.entity.Todo;
import com.api.exception.ResourceNotFoundException;
import com.api.repository.TodoRepo;
import com.api.service.TodoService;
import com.api.util.CommonUtil;


@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

	

	@Autowired
	private TodoService todoService;

	
	@PostMapping("/add")
	public ResponseEntity<?> saveTodo(@RequestBody TodoDto todo) throws Exception {
		Boolean saveTodo = todoService.saveTodo(todo);
		if (saveTodo) {
			return CommonUtil.createBuildResponse("Todo Saved Successfully", HttpStatus.CREATED);
		} else {
			return CommonUtil.createErrorResponseMessage("Todo not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTodo(@PathVariable Integer id) throws Exception {
		TodoDto toDoById = todoService.getToDoById(id);
		return CommonUtil.createBuildResponse(toDoById, HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<?> getAlltodo() throws Exception {
		List<TodoDto> todoList = todoService.getTodoByUser();

		if (!CollectionUtils.isEmpty(todoList)) {
			return ResponseEntity.noContent().build();

		} else {
			return CommonUtil.createBuildResponse(todoList, HttpStatus.OK);
		}
	}

}
