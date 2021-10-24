package pl.anime.scraper.domain.api.shinden.utils;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Element;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenAccessKey;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesTag;

public class ShindenUtils {

    public static ShindenAccessKey acquireAccessKey(String href) {
        var idAndName = acquireIdAndNameFromHref(href);
        return new ShindenAccessKey(idAndName.getId(), idAndName.getName());
    }

    public static IdAndName acquireIdAndNameFromHref(String href) {
        var hrefList = Arrays.stream(href.split("/"))
                .filter(el -> !el.isBlank())
                .collect(Collectors.toList());
        var idAndName = new IdAndName();
        var idAndNames = hrefList.get(1).split("-");
        idAndName.setId(Long.valueOf(Arrays.asList(idAndNames).get(0)));
        idAndName.setName(String.join("-", Arrays.asList(idAndNames).subList(1, idAndNames.length)));
        return idAndName;
    }

    public static ShindenSeriesTag createTagFromHtmlATag(Element aTag) {
        var tag = new ShindenSeriesTag();
        var idAndName = ShindenUtils.acquireIdAndNameFromHref(aTag.attr("href"));
        tag.setKey(new ShindenAccessKey(idAndName.getId(), idAndName.getName()));
        tag.setText(aTag.text());
        tag.setDescription(aTag.attr("title").trim().replaceAll("\n", ""));
        return tag;
    }

    @Getter
    @Setter
    public static class IdAndName {

        private Long id;
        private String name;
    }
}
