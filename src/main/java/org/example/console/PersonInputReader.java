package org.example.console;

import org.example.domain.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class PersonInputReader {
//Вводит данные объекта Person
    private final Scanner scanner;

    public PersonInputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public Person readPerson() {

        String name = readName();
        Coordinates coordinates = readCoordinates();
        float height = readHeight();
        LocalDateTime birthday = readBirthday();
        Color hairColor = readColor();
        Country nationality = readCountry();
        Location location = readLocation();

        return new Person(
                name,
                coordinates,
                height,
                birthday,
                hairColor,
                nationality,
                location
        );
    }

    private String readName() {
        while (true) {
            System.out.print("Введите имя: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Имя обязательно для заполнения.");
                continue;
            }

            return input;
        }
    }

    private Coordinates readCoordinates() {
        long x;
        double y;

        while (true) {
            try {
                System.out.print("Введите координату X (целое число): ");
                x = Long.parseLong(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести целое число. Попробуйте ещё раз.");
            }
        }

        while (true) {
            try {
                System.out.print("Введите координату Y (дробное число, максимум 663): ");
                y = Double.parseDouble(scanner.nextLine().trim());

                if (y > 663) {
                    System.out.println("Значение превышает допустимый максимум (663).");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести число. Попробуйте ещё раз.");
            }
        }

        return new Coordinates(x, y);
    }

    private float readHeight() {
        while (true) {
            try {
                System.out.print("Введите рост (положительное число): ");
                float height = Float.parseFloat(scanner.nextLine().trim());

                if (height <= 0) {
                    System.out.println("Рост должен быть положительным числом.");
                    continue;
                }

                return height;
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести число. Попробуйте ещё раз.");
            }
        }
    }

    private LocalDateTime readBirthday() {
        while (true) {
            System.out.print("Введите дату рождения в формате yyyy-MM-ddTHH:mm или нажмите Enter для пропуска: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                return LocalDateTime.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты. Пример: 2001-05-21T18:30");
            }
        }
    }

    private Color readColor() {
        while (true) {
            System.out.println("Доступные варианты цвета волос:");
            for (Color color : Color.values()) {
                System.out.println("- " + color);
            }

            System.out.print("Введите цвет или нажмите Enter для пропуска: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                return Color.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Такого варианта нет в списке. Выберите один из предложенных.");
            }
        }
    }

    private Country readCountry() {
        while (true) {
            System.out.println("Доступные варианты гражданства:");
            for (Country country : Country.values()) {
                System.out.println("- " + country);
            }

            System.out.print("Введите страну или нажмите Enter для пропуска: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                return Country.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Такого варианта нет в списке. Выберите один из предложенных.");
            }
        }
    }

    private Location readLocation() {

        System.out.print("Введите location.x (дробное число) или нажмите Enter для пропуска: ");
        String inputX = scanner.nextLine().trim();

        if (inputX.isEmpty()) {
            return null;
        }

        double x;

        try {
            x = Double.parseDouble(inputX);
        } catch (NumberFormatException e) {
            System.out.println("Нужно ввести число. Поле Location будет пропущено.");
            return null;
        }

        Double y;
        while (true) {
            try {
                System.out.print("Введите location.y (обязательное число): ");
                y = Double.parseDouble(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести число. Попробуйте ещё раз.");
            }
        }

        Integer z;
        while (true) {
            try {
                System.out.print("Введите location.z (обязательное целое число): ");
                z = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Нужно ввести целое число. Попробуйте ещё раз.");
            }
        }

        return new Location(x, y, z);
    }
}