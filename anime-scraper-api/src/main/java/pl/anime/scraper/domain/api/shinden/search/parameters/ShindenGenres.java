package pl.anime.scraper.domain.api.shinden.search.parameters;

import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShindenGenres implements ShindenTagParam {
    ACTION(5),
    ADVENTURE(6),
    COMEDY(7),
    CRIMINAL(20),
    CYBERPUNK(106),
    DRAMA(8),
    ECCHI(78),
    EXPERIMENTAL(1747),
    FANTASY(22),
    HAREM(130),
    HENTAI(234),
    HISTORICAL(92),
    HORROR(51),
    MADNESS(97),
    MAGIC(18),
    MARTIAL_ARTS(57),
    MECHA(98),
    MILITARY(93),
    MUSIC(136),
    MYSTERY(12),
    PARODY(165),
    PSYCHOLOGICAL(52),
    REVERSE_HAREM(263),
    SLICE_OF_LIFE(42),
    SUPERNATURAL(19),
    ROMANCE(2672),
    ROMANCE_TO_SPLIT(38),
    SCI_FI(549),
    SCHOOL(65),
    SHOUJO_AI(167),
    SHOUNEN_AI(207),
    SPACE_OPERA(384),
    SPORTS(31),
    STEAMPUNK(1734),
    THRILLER(53),
    YAOI(364),
    YURI(380);

    private final Integer value;

    public static Optional<ShindenGenres> findById(Integer value) {
        return Arrays.stream(values()).filter(genre -> genre.value.equals(value)).findFirst();
    }
}
