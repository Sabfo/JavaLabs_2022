package lab2.registration.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab2.registration.model.Instructor;

import java.io.File;
import java.io.IOException;

/**
 * Класс для чтения информации о инструкторах из файлов
 */
public class InstructorDataReader {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @return список инструкторов
     */
    public Instructor[] readInstructorData() throws IOException {
        return objectMapper.readValue(new File("src/main/resources/instructors.json"), Instructor[].class);
    }

}
