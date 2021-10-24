package pl.anime.scraper.domain.api.shinden.search.parameters;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShindenTargetGroups implements ShindenTagParam {
    FOR_CHILDREN(218),
    JOSEI(39),
    SEINEN(48),
    SHOUJO(128),
    SHOUNEN(23);

    private final Integer value;

    public static Optional<ShindenTargetGroups> findById(Integer value) {
        return Arrays.stream(values()).filter(targetGroup -> targetGroup.value.equals(value)).findFirst();
    }
}
