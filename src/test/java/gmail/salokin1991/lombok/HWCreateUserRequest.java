package gmail.salokin1991.lombok;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HWCreateUserRequest {

    private String name;
    private String job;

}
