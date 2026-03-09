package org.example.console;

import org.example.service.PersonService;
import org.example.workfiles.CollectionFileManager;

import java.util.Scanner;

public class InteractiveConsole {
 //отвечает за ввод команд пользователя
    private final Scanner scanner;
    private final CommandProcessor commandProcessor;

    public InteractiveConsole(PersonService personService,
                              CollectionFileManager fileManager) {

        this.scanner = new Scanner(System.in);
        this.commandProcessor =
                new CommandProcessor(personService, scanner, fileManager);
    }

    public void start() {
        System.out.println("Программа запущена. Введите 'help' для списка команд.");

        while (true) {
            System.out.print("> ");

            String input;

            try {
                input = scanner.nextLine().trim();
            } catch (Exception e) {
                System.out.println("Ошибка чтения ввода.");
                continue;
            }

            if (input.isEmpty()) {
                continue;
            }

            try {
                boolean shouldExit = commandProcessor.process(input);

                if (shouldExit) {
                    System.out.println("Завершение программы...");
                    break;
                }

            } catch (Exception e) {
                System.out.println("Ошибка выполнения команды: " + e.getMessage());
            }
        }

        scanner.close();
    }
}