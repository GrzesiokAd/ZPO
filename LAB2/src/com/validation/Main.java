package com.validation;

import com.validation.exception.ValidationException;
import com.validation.validator.Validator;

public class Main {
    public static void main(String[] args) {
        testInvalidStudent();
        testValidStudent();
    }

    private static void testInvalidStudent() {
        try {
            Student student = new Student(null, "A", "154", "");
            Validator.validate(student);
        } catch (ValidationException e) {
            System.out.println("Błędy walidacji:");
            System.out.println(e.getMessage());
        }
    }

    private static void testValidStudent() {
        try {
            Student student = new Student("Jan", "Kowalski", "12345678", "jan.kowalski@pbs.edu.pl");
            Validator.validate(student);
            System.out.println("Walidacja zakończona poprawnie");
            System.out.println(student);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
