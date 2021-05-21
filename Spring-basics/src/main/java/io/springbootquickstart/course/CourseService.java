package io.springbootquickstart.course;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CourseService {

	@Autowired
	private CourseRepository repo;
	
	public List<Course> getAllTopics(String topicId) {
		ArrayList<Course> topics = new ArrayList<>();
		repo.findByTopicId(topicId).forEach(a -> topics.add(a));
		return topics;
	}
	
	public Course getCourse(String id) {
		return repo.findById(id).get();
	}
	
	public void addCourse(Course course) {
		repo.save(course);
	}

	public void updateCourse(Course course, String id) {
		repo.save(course);
	}

	public void deleteCourse(String id) {
		repo.deleteById(id);
	}
}
