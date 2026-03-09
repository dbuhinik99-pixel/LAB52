package org.example.console;

import org.example.domain.Person;
import org.example.service.PersonService;
import org.example.workfiles.CollectionFileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// Обрабатывает команды пользователя.

public class CommandProcessor {

    private final PersonService personService;
    private final PersonInputReader personInputReader;
    private final CollectionFileManager fileManager;

    // защита от рекурсивного запуска скриптов
    private final Set<String> executingScripts = new HashSet<>();

    public CommandProcessor(PersonService personService,
                            Scanner scanner,
                            CollectionFileManager fileManager) {

        this.personService = personService;
        this.personInputReader = new PersonInputReader(scanner);
        this.fileManager = fileManager;
    }

    public boolean process(String input) {

        String[] parts = input.split(" ", 2);
        String command = parts[0];

        switch (command) {

            case "help":
                printHelp();
                break;

            case "info":
                System.out.println(personService.info());
                break;

            case "show":
                if (personService.show().isEmpty()) {
                    System.out.println("Коллекция пуста.");
                } else {
                    personService.show().forEach(System.out::println);
                }
                break;

            case "add":
                Person newPerson = personInputReader.readPerson();
                personService.add(newPerson);
                System.out.println("Элемент добавлен.");
                break;

            case "update":
                if (parts.length < 2) {
                    System.out.println("Укажите id.");
                    break;
                }
                try {
                    int id = Integer.parseInt(parts[1]);
                    Person updatedPerson = personInputReader.readPerson();
                    if (personService.update(id, updatedPerson)) {
                        System.out.println("Элемент обновлён.");
                    } else {
                        System.out.println("Элемент с таким id не найден.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("id должен быть числом.");
                }
                break;

            case "remove_by_id":
                if (parts.length < 2) {
                    System.out.println("Укажите id.");
                    break;
                }
                try {
                    int id = Integer.parseInt(parts[1]);
                    if (personService.removeById(id)) {
                        System.out.println("Элемент удалён.");
                    } else {
                        System.out.println("Элемент с таким id не найден.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("id должен быть числом.");
                }
                break;

            case "clear":
                personService.clear();
                System.out.println("Коллекция очищена.");
                break;

            case "save":
                fileManager.save(personService.getCollection());
                System.out.println("Коллекция сохранена.");
                break;

            case "add_if_min":
                Person minPerson = personInputReader.readPerson();
                if (personService.addIfMin(minPerson)) {
                    System.out.println("Элемент добавлен как минимальный.");
                } else {
                    System.out.println("Элемент не меньше текущего минимального.");
                }
                break;

            case "remove_greater":
                Person greaterPerson = personInputReader.readPerson();
                int removedGreater = personService.removeGreater(greaterPerson);
                System.out.println("Удалено элементов: " + removedGreater);
                break;

            case "remove_lower":
                Person lowerPerson = personInputReader.readPerson();
                int removedLower = personService.removeLower(lowerPerson);
                System.out.println("Удалено элементов: " + removedLower);
                break;

            case "filter_starts_with_name":
                if (parts.length < 2) {
                    System.out.println("Укажите подстроку.");
                    break;
                }
                var filtered = personService.filterStartsWithName(parts[1]);
                if (filtered.isEmpty()) {
                    System.out.println("Совпадений не найдено.");
                } else {
                    filtered.forEach(System.out::println);
                }
                break;

            case "print_ascending":
                personService.printAscending()
                        .forEach(System.out::println);
                break;

            case "print_descending":
                personService.printDescending()
                        .forEach(System.out::println);
                break;

            case "execute_script":

                if (parts.length < 2) {
                    System.out.println("Укажите имя файла.");
                    break;
                }

                String fileName = parts[1];

                if (executingScripts.contains(fileName)) {
                    System.out.println("Обнаружена рекурсия! Скрипт уже выполняется.");
                    break;
                }

                File scriptFile = new File(fileName);

                if (!scriptFile.exists()) {
                    System.out.println("Файл скрипта не найден.");
                    break;
                }

                executingScripts.add(fileName);

                try (Scanner scriptScanner = new Scanner(scriptFile)) {

                    while (scriptScanner.hasNextLine()) {

                        String line = scriptScanner.nextLine().trim();

                        if (line.isEmpty()) continue;

                        System.out.println("Выполнение: " + line);

                        boolean shouldExit = process(line);

                        if (shouldExit) {
                            executingScripts.remove(fileName);
                            return true;
                        }
                    }

                } catch (FileNotFoundException e) {
                    System.out.println("Ошибка чтения скрипта.");
                }

                executingScripts.remove(fileName);
                break;

            case "exit":
                System.out.println("Завершение программы без сохранения...");
                return true;

            default:
                System.out.println("Неизвестная команда. Введите 'help'.");
        }

        return false;
    }

    private void printHelp() {
        System.out.println("""
                help : вывести справку
                info : информация о коллекции
                show : показать все элементы
                add : добавить элемент
                update id : обновить элемент
                remove_by_id id : удалить по id
                clear : очистить коллекцию
                save : сохранить коллекцию в файл
                execute_script file_name : выполнить команды из файла
                exit : завершить программу
                add_if_min : добавить если меньше минимального
                remove_greater : удалить больше заданного
                remove_lower : удалить меньше заданного
                filter_starts_with_name name : фильтр по имени
                print_ascending : по возрастанию
                print_descending : по убыванию
                """);
    }
}