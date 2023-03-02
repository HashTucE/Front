package com.mediscreen.front.proxy;

import com.mediscreen.library.dto.NoteDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "note.ms", url = "${proxy.note.url}")
public interface NoteProxy {


    @GetMapping("/api/note/find/{id}")
    NoteDto findNoteById(@PathVariable int id);

    @PostMapping("/api/note/validate")
    NoteDto validateNote(@Valid @RequestBody NoteDto noteDto);

    @PostMapping("/api/note/update/{id}")
    NoteDto updateNote(@PathVariable int id, @Valid @RequestBody NoteDto noteDto);

    @PostMapping("/api/note/delete/{id}")
    void deleteNote(@PathVariable int id);
}
