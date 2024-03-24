package edu.iu.c322.midterm.controllers;

import edu.iu.c322.midterm.model.Question;
import edu.iu.c322.midterm.model.Quiz;
import edu.iu.c322.midterm.repository.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/quizzes")
public class QuizController {

    private final FileRepository fileRepository;

    public QuizController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @GetMapping
    public List<Quiz> getQuizzes() throws IOException {
        return fileRepository.findAllQuizzes();
    }

    @PostMapping
    public int addQuiz(@RequestBody Quiz quiz) throws IOException {
        return fileRepository.addQuiz(quiz);
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable int id) throws IOException {
        return fileRepository.findQuizbyID(id);
    }

    @PutMapping("/{id}")
    public void updateQuiz(@PathVariable int id, @RequestBody Quiz updatedQuiz) throws IOException {
        Quiz existingQuiz = fileRepository.findQuizbyID(id);
        if (existingQuiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }

        if (updatedQuiz.getTitle() != null && !updatedQuiz.getTitle().isEmpty()) {
            existingQuiz.setTitle(updatedQuiz.getTitle());
        }

        if (updatedQuiz.getQuestionIds() != null && !updatedQuiz.getQuestionIds().isEmpty()) {
            existingQuiz.setQuestionIds(updatedQuiz.getQuestionIds());
            existingQuiz.setQuestions(fileRepository.findQuestionsByIds(updatedQuiz.getQuestionIds()));
        }

        fileRepository.updateQuiz(existingQuiz);
    }
}
