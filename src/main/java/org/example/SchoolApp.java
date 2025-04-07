package org.example;

import java.util.Scanner;

public class SchoolApp {
    public static void main(String[] args) {
        School school = new School();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== School Management Menu ===");
            System.out.println("1. Add student");
            System.out.println("2. Add discipline to student");
            System.out.println("3. Add mark to discipline");
            System.out.println("4. Remove student");
            System.out.println("5. Remove discipline from student");
            System.out.println("6. Show all student information");
            System.out.println("7. Show average school performance");
            System.out.println("8. Export to file");
            System.out.println("9. Import from file");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();
                    school.addStudent(new Student(studentName));
                    break;
                case 2:
                    System.out.print("Student name: ");
                    String sName = scanner.nextLine();
                    Student student = findStudentByName(school, sName);
                    if (student != null) {
                        System.out.print("Discipline name: ");
                        String discName = scanner.nextLine();
                        student.addDiscipline(new Discipline(discName));
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 3:
                    System.out.print("Student name: ");
                    String stName = scanner.nextLine();
                    Student foundStudent = findStudentByName(school, stName);
                    if (foundStudent != null) {
                        System.out.print("Discipline name: ");
                        String dName = scanner.nextLine();
                        Discipline foundDisc = findDisciplineByName(foundStudent, dName);
                        if (foundDisc != null) {
                            System.out.print("Mark (0–100): ");
                            int mark = scanner.nextInt();
                            scanner.nextLine();
                            try {
                                foundDisc.addMark(mark);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid mark.");
                            }
                        } else {
                            System.out.println("Discipline not found.");
                        }
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 4:
                    System.out.print("Student name: ");
                    school.removeStudent(scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Student name: ");
                    String studName = scanner.nextLine();
                    Student stud = findStudentByName(school, studName);
                    if (stud != null) {
                        System.out.print("Discipline name: ");
                        String discName = scanner.nextLine();
                        stud.removeDiscipline(discName);
                        System.out.println("Discipline removed.");
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 6:
                    for (Student s : school.getStudents()) {
                        System.out.println("\nStudent: " + s.getName() + ", Average: " + s.averagePerformance());
                        for (Discipline d : s.getDisciplines()) {
                            System.out.println("  " + d.getName() + ": " + d.getMarks());
                        }
                    }
                    break;
                case 7:
                    System.out.println("Average school performance: " + school.averagePerformance());
                    break;
                case 8:
                    try {
                        school.exportToFile("school_data.txt");
                        System.out.println("Data exported to school_data.txt");
                    } catch (Exception e) {
                        System.out.println("Export error: " + e.getMessage());
                    }
                    break;
                case 9:
                    try {
                        school.importFromFile("school_data.txt");
                        System.out.println("Data imported from school_data.txt");
                    } catch (Exception e) {
                        System.out.println("Import error: " + e.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static Student findStudentByName(School school, String name) {
        return school.getStudents().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private static Discipline findDisciplineByName(Student student, String name) {
        return student.getDisciplines().stream()
                .filter(d -> d.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
