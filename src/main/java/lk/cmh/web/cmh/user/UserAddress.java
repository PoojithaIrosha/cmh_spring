package lk.cmh.web.cmh.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAddress {
    @Column(columnDefinition = "TEXT")
    private String contactName;
    @Column(columnDefinition = "TEXT")
    private String addressLine1;
    @Column(columnDefinition = "TEXT")
    private String addressLine2;
    private String city;
    private String postalCode;
    private String province;
    private String mobileNumber;
}
