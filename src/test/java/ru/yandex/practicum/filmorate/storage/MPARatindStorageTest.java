package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARating.MPARatingDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.MPAMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({MPARatingDbStorage.class, MPAMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MPARatindStorageTest {

    private final MPARatingDbStorage mpaRatingDbStorage;
    private static final int TEST_MPA_ID = 1;
    private static final int COUNT_MPA_IN_DB = 5;

    @Test
    @DisplayName("Должен вернуть соответствующий id рейтинг.")
    public void getMpaByIdTest() {
        MPARating mpaToCheck = MPARating.builder().id(1).name("G").build();
        Optional<MPARating> mpa = mpaRatingDbStorage.getMPAById(TEST_MPA_ID);
        assertThat(mpa)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(mpaToCheck);
    }

    @Test
    @DisplayName("Должен возвращать все рейтинги.")
    public void getAllMpa() {
        List<MPARating> mpas = mpaRatingDbStorage.getAllMPA();
        assertThat(mpas.size()).isEqualTo(COUNT_MPA_IN_DB);
    }
}