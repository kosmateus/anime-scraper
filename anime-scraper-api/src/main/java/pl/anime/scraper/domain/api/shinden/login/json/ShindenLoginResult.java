package pl.anime.scraper.domain.api.shinden.login.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShindenLoginResult {

    private ShindenLoginStatus status;
    private String loginDetails;

    @JsonIgnore
    public boolean isSuccess(){
        return ShindenLoginStatus.SUCCESS == status;
    }

}
