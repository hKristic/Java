package io.springbootquickstart.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.springbootquickstart.topic.Topic;

@RestController
public class CourseController {

	@Autowired
	private CourseService course;
	
	@RequestMapping("/topics/{id}/courses")
	public List<Course> getAllCourses() {
		return course.getAllTopics();
	}
	
	@RequestMapping("/topics/{id}/courses/{courseId}")
	public Course getCourse(@PathVariable String courseId) {
		return course.getCourse(courseId);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/topics/{id}/courses")
	public void addCourse(@RequestBody Course c, @PathVariable String id) {
		c.setTopic(new Topic(id, "", ""));
		course.addCourse(c);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/topics/{id}/courses/{courseId}")
	public void updateCourse(@RequestBody Course c, @PathVariable String courseId, @PathVariable String id) {
		c.setTopic(new Topic(id, "", ""));
		course.updateCourse(c, courseId);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/topics/{id}/courses/{courseId}")
	public void deleteCourse(@PathVariable String courseId) {
		course.deleteCourse(courseId);
	}
}
