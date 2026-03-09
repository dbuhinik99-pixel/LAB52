package org.example;

import org.example.console.InteractiveConsole;
import org.example.service.PersonService;
import org.example.workfiles.CollectionFileManager;

//Точка входа в программу.
//Отвечает за:
//- получение имени файла из переменной окружения
//- загрузку коллекции из файла
//- запуск интерактивного режима

public class Main {

    public static void main(String[] args) {

        // Получаем имя файла из переменной окружения
        String fileName = System.getenv("LABA_FILE");

        if (fileName == null || fileName.isBlank()) {
            System.out.println("Переменная окружения LABA_FILE не задана.");
            System.out.println("Программа завершена.");
            return;
        }

        try {
            // Создание менеджера файла
            CollectionFileManager fileManager =
                    new CollectionFileManager(fileName);

            // Создание сервиса
            PersonService personService = new PersonService();

            // Автоматическая загрузка коллекции из файла
            personService.getCollection().addAll(fileManager.load());

            System.out.println("Коллекция успешно загружена.");
            System.out.println("Запуск интерактивного режима...");

            // Запуск консоли
            InteractiveConsole console =
                    new InteractiveConsole(personService, fileManager);

            console.start();

        } catch (Exception e) {
            System.out.println("Ошибка запуска программы: " + e.getMessage());
        }
    }
}