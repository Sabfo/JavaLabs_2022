package lab2.registration.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab2.registration.model.CourseInfo;
import lab2.registration.model.CourseInstance;

import java.io.File;
import java.io.IOException;

/**
 * Класс для чтения информации о курсах из файлов
 */
public class CourseDataReader {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @return список информации о каждой дисциплине
     */
    public CourseInfo[] readBachelorStudentData() throws IOException {
        return objectMapper.readValue(new File("src/main/resources/courseInfos.json"), CourseInfo[].class);
    }

    /**
     * @return список курсов
     */
    public CourseInstance[] readMasterStudentData() throws IOException {
        return objectMapper.readValue(new File("src/main/resources/courseInstances.json"), CourseInstance[].class);
    }

}
