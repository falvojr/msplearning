package com.msplearning.rest.app;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.msplearning.entity.Student;
import com.msplearning.rest.app.generic.GenericCrudRestService;
import com.msplearning.service.StudentService;
import com.msplearning.service.generic.GenericCrudService;

/**
 * The StudentRestService class provides the RESTful services of entity {@link Student}.
 *
 * @author Venilton Falvo Junior (veniltonjr)
 */
@Component
@Path("/student")
public class StudentRestService extends GenericCrudRestService<Student, Long> {

	@Autowired
	private StudentService studentService;

	@Override
	protected GenericCrudService<Student, Long> getService() {
		return this.studentService;
	}
}
