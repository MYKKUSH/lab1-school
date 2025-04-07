package org.example;
import java.io.*;
import java.util.*;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class Discipline {
    private String name;
    private List<Integer> marks;

    public Discipline(String name) {
        this.name = name;
        marks = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<Integer> getMarks() { return marks; }

    public void addMark(int mark) {
        if (mark >= 0 && mark <= 100) {
            marks.add(mark);
        } else {
            throw new IllegalArgumentException("Mark must be between 0 and 100");
        }
    }

    public double averageMark() {
        return marks.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Discipline that = (Discipline) obj;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() { return Objects.hash(name); }
}

class Student {
    private String name;
    private List<Discipline> disciplines;

    public Student(String name) {
        this.name = name;
        disciplines = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<Discipline> getDisciplines() { return disciplines; }

    public void addDiscipline(Discipline discipline) {
        disciplines.add(discipline);
    }
    public void removeDiscipline(String disciplineName) {
        disciplines.removeIf(d -> d.getName().equalsIgnoreCase(disciplineName));
    }

    public double averagePerformance() {
        return disciplines.stream()
                .mapToDouble(Discipline::averageMark)
                .average()
                .orElse(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() { return Objects.hash(name); }
}

class School {
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) { students.add(student); }
    public void removeStudent(String name) {
        students.removeIf(s -> s.getName().equals(name));
    }

    public List<Student> getStudents() { return students; }

    public double averagePerformance() {
        return students.stream()
                .mapToDouble(Student::averagePerformance)
                .average()
                .orElse(0);
    }

    public void exportToFile(String fileName) throws IOException {
        students.sort(Comparator.comparing(Student::averagePerformance).reversed());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Student student : students) {
                writer.write("Student: " + student.getName() + ", Average: " + student.averagePerformance());
                writer.newLine();
                for (Discipline d : student.getDisciplines()) {
                    writer.write("  Discipline: " + d.getName() + ", Marks: " + d.getMarks());
                    writer.newLine();
                }
            }
        }
    }

    public void importFromFile(String fileName) throws IOException {
        students.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            Student currentStudent = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Student: ")) {
                    String name = line.split(",")[0].split(": ")[1];
                    currentStudent = new Student(name);
                    students.add(currentStudent);
                } else if (line.startsWith("  Discipline: ") && currentStudent != null) {
                    String[] parts = line.trim().split(", Marks: ");
                    String discName = parts[0].split(": ")[1];
                    Discipline discipline = new Discipline(discName);
                    if (parts.length > 1) {
                        String[] marks = parts[1].replace("[", "").replace("]", "").split(", ");
                        for (String m : marks) discipline.addMark(Integer.parseInt(m));
                    }
                    currentStudent.addDiscipline(discipline);
                }
            }
        }
    }

    public static void main(String[] args) {
        School school = new School();

        Student s1 = new Student("Oleg Koval");
        Discipline math = new Discipline("Math");
        math.addMark(85);
        math.addMark(90);

        Discipline phys = new Discipline("Physics");
        phys.addMark(78);

        s1.addDiscipline(math);
        s1.addDiscipline(phys);

        school.addStudent(s1);

        Student s2 = new Student("Iryna Shevchenko");
        Discipline lit = new Discipline("Literature");
        lit.addMark(95);
        s2.addDiscipline(lit);
        school.addStudent(s2);

        System.out.println("Average: " + school.averagePerformance());

        for (Student s : school.getStudents()) {
            System.out.println("Student: " + s.getName() + ", Progress: " + s.averagePerformance());
        }

        try {
            school.exportToFile("school_data.txt");
            System.out.println("Exported in school_data.txt");

            // Демонстрація імпорту з файлу
            School importedSchool = new School();
            importedSchool.importFromFile("school_data.txt");
            System.out.println("Imported students from file:");
            for (Student s : importedSchool.getStudents()) {
                System.out.println(" - " + s.getName() + ", Score: " + s.averagePerformance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
