
package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;


class SchoolSystemTests {

    @Test
    void testAddMarkAndAverage() {
        Discipline math = new Discipline("Math");
        math.addMark(80);
        math.addMark(90);
        assertEquals(85.0, math.averageMark());
    }

    @Test
    void testInvalidMarkThrowsException() {
        Discipline math = new Discipline("Math");
        assertThrows(IllegalArgumentException.class, () -> math.addMark(150));
    }

    @Test
    void testStudentAveragePerformance() {
        Student student = new Student("Test");
        Discipline math = new Discipline("Math");
        math.addMark(100);
        Discipline physics = new Discipline("Physics");
        physics.addMark(80);
        student.addDiscipline(math);
        student.addDiscipline(physics);
        assertEquals(90.0, student.averagePerformance());
    }

    @Test
    void testStudentEquality() {
        Student s1 = new Student("Oleh");
        Student s2 = new Student("Oleh");
        assertEquals(s1, s2);
    }

    @Test
    void testSchoolAveragePerformance() {
        School school = new School();
        Student s1 = new Student("A");
        Discipline d1 = new Discipline("Math");
        d1.addMark(100);
        s1.addDiscipline(d1);
        school.addStudent(s1);

        Student s2 = new Student("B");
        Discipline d2 = new Discipline("Physics");
        d2.addMark(50);
        s2.addDiscipline(d2);
        school.addStudent(s2);

        assertEquals(75.0, school.averagePerformance());
    }

    @Test
    void testRemoveStudent() {
        School school = new School();
        Student s = new Student("Oleh");
        school.addStudent(s);
        school.removeStudent("Oleh");
        assertTrue(school.getStudents().isEmpty());
    }

    @Test
    void testExportAndImport() throws IOException {
        School school = new School();
        Student s = new Student("Oleh");
        Discipline d = new Discipline("Math");
        d.addMark(90);
        s.addDiscipline(d);
        school.addStudent(s);

        File tempFile = File.createTempFile("school_test", ".txt");
        tempFile.deleteOnExit();

        school.exportToFile(tempFile.getAbsolutePath());

        School imported = new School();
        imported.importFromFile(tempFile.getAbsolutePath());

        List<Student> students = imported.getStudents();
        assertEquals(1, students.size());
        assertEquals("Oleh", students.get(0).getName());
        assertEquals(90.0, students.get(0).averagePerformance());
    }

    @Test
    void testDisciplineEqualsAndHashCode() {
        Discipline d1 = new Discipline("Math");
        Discipline d2 = new Discipline("Math");
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void testEmptyDisciplineAverage() {
        Discipline d = new Discipline("Empty");
        assertEquals(0.0, d.averageMark());
    }
}
