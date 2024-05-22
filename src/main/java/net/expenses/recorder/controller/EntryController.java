package net.expenses.recorder.controller;

import lombok.RequiredArgsConstructor;
import net.expenses.recorder.dao.User;
import net.expenses.recorder.dto.APIResponseDto;
import net.expenses.recorder.dto.EntryDto;
import net.expenses.recorder.dto.EntryFormDto;
import net.expenses.recorder.dto.EntryModifyFormDto;
import net.expenses.recorder.dto.EntryMonthDto;
import net.expenses.recorder.dto.EntryReferenceDto;
import net.expenses.recorder.dto.EntryYearDto;
import net.expenses.recorder.dto.ResponseDto;
import net.expenses.recorder.service.EntryMonthService;
import net.expenses.recorder.service.EntryService;
import net.expenses.recorder.service.EntryYearService;
import net.expenses.recorder.utils.CommonApiConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Kazi Tanvir Azad
 */
@RestController
@RequestMapping(path = CommonApiConstants.ENTRY_API)
@RequiredArgsConstructor
public class EntryController implements CommonApiConstants {
    private final EntryService entryService;
    private final EntryYearService entryYearService;
    private final EntryMonthService entryMonthService;

    @PostMapping(path = CREATE_API, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> createEntry(@RequestBody EntryFormDto entryForm) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EntryReferenceDto entryReference = entryService.createEntry(user, entryForm);
        return new ResponseEntity<>(APIResponseDto.builder()
                .setMessage("Entry created successfully!")
                .setData(entryReference)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping(path = FETCH_ALL_API, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> fetchAllEntries(@RequestParam(name = "year") String year,
                                                       @RequestParam(name = "month", required = false) String month) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EntryDto> entryDtos = month != null ?
                entryService.getAllEntriesByEntryMonth(user, year, month) :
                entryService.getAllEntriesByEntryYear(user, year);
        return ResponseEntity.ok(APIResponseDto.builder()
                .setData(entryDtos)
                .build());
    }

    @GetMapping(path = GROUP_BY_API + BASE + "year", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> fetchEntriesByYear() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EntryYearDto> entryYearDtos = entryYearService.getAllEntryYear(user);
        return ResponseEntity.ok(APIResponseDto.builder()
                .setData(entryYearDtos)
                .build());
    }

    @GetMapping(path = GROUP_BY_API + BASE + "month", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> fetchEntriesByMonth(@RequestParam(name = "year") String year) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EntryMonthDto> entryMonthDtos = entryMonthService.getAllEntryMonth(user, year);
        return ResponseEntity.ok(APIResponseDto.builder()
                .setData(entryMonthDtos)
                .build());
    }

    @PutMapping(path = UPDATE_API, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> modifyEntry(@RequestBody EntryModifyFormDto entryModifyForm) {
        EntryReferenceDto entryReference = entryService.modifyEntry(entryModifyForm);
        return new ResponseEntity<>(APIResponseDto.builder()
                .setMessage("Entry updated successfully!")
                .setData(entryReference)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping(path = FETCH_API, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> getEntryByReference(@RequestParam(name = "entry_id") String entryId) {
        EntryDto entryDto = entryService.getEntryById(entryId);
        return ResponseEntity.ok(APIResponseDto.builder()
                .setData(entryDto)
                .build());
    }
}
