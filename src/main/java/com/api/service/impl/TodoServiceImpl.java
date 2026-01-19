package com.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.api.dto.TodoDto;
import com.api.dto.TodoDto.StatusDto;
import com.api.entity.Todo;
import com.api.enums.TodoStatus;
import com.api.exception.ResourceNotFoundException;
import com.api.repository.TodoRepo;
import com.api.service.TodoService;
import com.api.util.Validation;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepo todoRepo;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Validation validation;

	@Override
	public Boolean saveTodo(TodoDto todoDto) throws ResourceNotFoundException {
		validation.todoValidation(todoDto);
		Todo todo = mapper.map(todoDto, Todo.class);
		todo.setStatusId(todoDto.getStatus().getId());
		Todo saveTodo = todoRepo.save(todo);
		if (!ObjectUtils.isEmpty(saveTodo)) {
			return true;
		}

		return false;
	}

	@Override
	public TodoDto getToDoById(Integer id) throws ResourceNotFoundException {
		Todo todo = todoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id is Invalid ,Not found"));
		TodoDto todoDto = mapper.map(todo, TodoDto.class);
		setStatus(todoDto , todo);

		return todoDto;
	}

	private void setStatus(TodoDto todoDto, Todo todo) {
		for(TodoStatus st : TodoStatus.values())
		{
			if(st.getId().equals(todo.getStatusId()))
			{
				StatusDto statusDto = StatusDto.builder()
						.id(st.getId())
						.name(st.getName())
			            .build();
				todoDto.setStatus(statusDto);
			}
		}
	}

	@Override
	public List<TodoDto> getTodoByUser() {
		Integer userId = 1;

		List<Todo> todos = todoRepo.findByCreatedBy(userId);
		return todos.stream().map(td -> mapper.map(td, TodoDto.class)).toList();

	}

}
