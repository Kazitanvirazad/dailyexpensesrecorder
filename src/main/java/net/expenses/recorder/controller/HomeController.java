package net.expenses.recorder.controller;

import net.expenses.recorder.utils.CommonApiConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * @author Kazi Tanvir Azad
 */
@RestController
@RequestMapping(path = CommonApiConstants.BASE_APP)
public class HomeController implements CommonApiConstants {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping(path = STATUS_API, produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getAppStatus() {
        return ResponseEntity.ok(applicationName + " is online. Time: " + Instant.now());
    }
}
