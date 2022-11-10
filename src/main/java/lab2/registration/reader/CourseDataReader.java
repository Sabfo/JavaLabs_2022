package lab2.registration.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lab2.registration.model.CourseInfo;
import lab2.registration.model.CourseInstance;

import java.io.File;
import java.io.IOException;

/**
 * Класс для чтения информации о курсах из файлов
 */
public class CourseDataReader {

    private ObjectMapper objectMapper = new ObjectMapper();

    public CourseDataReader() {
        objectMapper.registerModule(new JavaTimeModule()); // Для работы с датами в json
    }
    /**
     * @return список информации о каждой дисциплине
     */
    public CourseInfo[] readCourseInfoData() throws IOException {
        return objectMapper.readValue(new File("src/main/resources/courseInfos.json"), CourseInfo[].class);
    }

    /**
     * @return список курсов
     */
    public CourseInstance[] readCourseInstanceData() throws IOException {
        return objectMapper.readValue(new File("src/main/resources/courseInstances.json"), CourseInstance[].class);
    }

}
