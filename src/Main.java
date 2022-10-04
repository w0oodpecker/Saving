import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        String PATH = "D://Progs/Saving/savegames";
        String FILENAME = "save";
        String ZIPFILE = "zip_output.zip";
        String EXTENSION = ".dat";

        createFolder(PATH); //Создаем каталог для сохранений
        ArrayList<String> saves = new ArrayList<>();
        ArrayList<GameProgress> progresses = new ArrayList<>();
        progresses.add(new GameProgress(1, 1, 1, 1));
        progresses.add(new GameProgress(2, 2, 2, 2));
        progresses.add(new GameProgress(3, 3, 3, 3));

        for (int index = 0; index < progresses.size(); index++) {
            String path = PATH + "/" + FILENAME + index + EXTENSION;
            if (saveGame(path, progresses.get(index))) {
                saves.add(path);
            }
        }
        zipFiles(PATH + "/" + ZIPFILE, saves);
        deleteFiles(saves);

    }


    //Сохранение экземпляра класса
    static boolean saveGame(String filePath, GameProgress gameProgress) {

        boolean success = false;

        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            success = true;
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            success = false;
        }
        return success;
    }


    //Упаковка сохранений в Zip
    static boolean zipFiles(String archFile, ArrayList<String> savesList) {
        boolean success = false;
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(archFile))) {
            for (int index = 0; index < savesList.size(); index++) {
                String[] substr = savesList.get(index).split("/");
                FileInputStream fis = new FileInputStream(savesList.get(index)); //Создаем поток для архивируемого файла
                ZipEntry entry = new ZipEntry(substr[substr.length - 1]); //Создаем экземпляр для записей в архив
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()]; //Читаем в буфер файл для архивации
                fis.read(buffer);
                //добавляем содержимое к архиву
                zout.write(buffer);
                //закрываем текущую запись для новой записи
                zout.closeEntry();
                fis.close();
            }
            success = true;
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            success = false;
        }
        return success;
    }


    //Удаление файлов
    static void deleteFiles(ArrayList<String> saveList) {
        for (int index = 0; index < saveList.size(); index++) {
            File file = new File(saveList.get(index).toString());
            if (file.exists()) {
                file.delete();
            }
        }
    }


    //Создание каталога
    static void createFolder(String pathFolder) {
        File folder = new File(pathFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

}