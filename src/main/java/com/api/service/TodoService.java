package com.api.service;

import java.util.List;

import com.api.dto.TodoDto;
import com.api.exception.ResourceNotFoundException;


public interface TodoService {
	
	public Boolean saveTodo(TodoDto todo) throws ResourceNotFoundException;
	public  TodoDto getToDoById(Integer id) throws ResourceNotFoundException;
	public List<TodoDto> getTodoByUser();

}
