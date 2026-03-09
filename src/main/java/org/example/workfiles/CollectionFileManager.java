package org.example.workfiles;

import org.example.domain.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//Класс отвечает за чтение и запись коллекции Person в XML-файл.

public class CollectionFileManager {

    private final String fileName;

    public CollectionFileManager(String fileName) {
        this.fileName = fileName;
    }

    //Загружает коллекцию из XML-файла.

    public Set<Person> load() {

        Set<Person> collection = new HashSet<>();
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Файл не найден. Будет создан новый при сохранении.");
            return collection;
        }

        if (!file.canRead()) {
            System.out.println("Нет прав на чтение файла.");
            return collection;
        }

        try (BufferedInputStream bis =
                     new BufferedInputStream(new FileInputStream(file))) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(bis);

            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("person");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Element element = (Element) nodeList.item(i);

                int id = Integer.parseInt(getTagValue(element, "id"));
                String name = getTagValue(element, "name");

                long coordX = Long.parseLong(getTagValue(element, "coordX"));
                double coordY = Double.parseDouble(getTagValue(element, "coordY"));

                float height = Float.parseFloat(getTagValue(element, "height"));

                String birthdayStr = getTagValue(element, "birthday");
                LocalDateTime birthday =
                        birthdayStr.isEmpty() ? null : LocalDateTime.parse(birthdayStr);

                String hairStr = getTagValue(element, "hairColor");
                Color hairColor =
                        hairStr.isEmpty() ? null : Color.valueOf(hairStr);

                String natStr = getTagValue(element, "nationality");
                Country nationality =
                        natStr.isEmpty() ? null : Country.valueOf(natStr);

                Location location = null;
                String locX = getTagValue(element, "locX");

                if (!locX.isEmpty()) {
                    double lx = Double.parseDouble(locX);
                    Double ly = Double.parseDouble(getTagValue(element, "locY"));
                    Integer lz = Integer.parseInt(getTagValue(element, "locZ"));
                    location = new Location(lx, ly, lz);
                }

                Person person = new Person(
                        id,
                        name,
                        new Coordinates(coordX, coordY),
                        height,
                        birthday,
                        hairColor,
                        nationality,
                        location
                );

                collection.add(person);
            }

        } catch (IOException | ParserConfigurationException |
                 SAXException | IllegalArgumentException e) {

            System.out.println("Ошибка загрузки файла: " + e.getMessage());
        }

        return collection;
    }

    //Сохраняет коллекцию в XML-файл.

    public void save(Set<Person> collection) {

        File file = new File(fileName);

        if (file.exists() && !file.canWrite()) {
            System.out.println("Нет прав на запись файла.");
            return;
        }

        try (BufferedOutputStream bos =
                     new BufferedOutputStream(new FileOutputStream(file))) {

            StringBuilder sb = new StringBuilder();
            sb.append("<persons>\n");

            for (Person p : collection) {

                sb.append("  <person>\n");

                sb.append("    <id>")
                        .append(p.getId())
                        .append("</id>\n");

                sb.append("    <name>")
                        .append(p.getName())
                        .append("</name>\n");

                sb.append("    <coordX>")
                        .append(p.getCoordinates().getX())
                        .append("</coordX>\n");

                sb.append("    <coordY>")
                        .append(p.getCoordinates().getY())
                        .append("</coordY>\n");

                sb.append("    <height>")
                        .append(p.getHeight())
                        .append("</height>\n");

                sb.append("    <birthday>")
                        .append(p.getBirthday() == null ? "" : p.getBirthday())
                        .append("</birthday>\n");

                sb.append("    <hairColor>")
                        .append(p.getHairColor() == null ? "" : p.getHairColor())
                        .append("</hairColor>\n");

                sb.append("    <nationality>")
                        .append(p.getNationality() == null ? "" : p.getNationality())
                        .append("</nationality>\n");

                if (p.getLocation() != null) {
                    sb.append("    <locX>").append(p.getLocation().getX()).append("</locX>\n");
                    sb.append("    <locY>").append(p.getLocation().getY()).append("</locY>\n");
                    sb.append("    <locZ>").append(p.getLocation().getZ()).append("</locZ>\n");
                } else {
                    sb.append("    <locX></locX>\n");
                }

                sb.append("  </person>\n");
            }

            sb.append("</persons>");

            bos.write(sb.toString().getBytes());
            bos.flush();

            System.out.println("Коллекция успешно сохранена.");

        } catch (IOException e) {
            System.out.println("Ошибка сохранения файла: " + e.getMessage());
        }
    }

    private String getTagValue(Element element, String tag) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) return "";
        return nodeList.item(0).getTextContent();
    }
}