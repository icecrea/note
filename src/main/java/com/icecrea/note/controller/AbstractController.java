package com.icecrea.note.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icecrea.note.util.JsonResult;

public class AbstractController {
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public JsonResult handleException(Exception e) {
		e.printStackTrace();
		return new JsonResult(e);
	}
}
