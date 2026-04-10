package hr.algebra.festlog.controller.mvc;

import hr.algebra.festlog.dto.EventDto;
import hr.algebra.festlog.entity.User;
import hr.algebra.festlog.enums.EventStatus;
import hr.algebra.festlog.enums.EventType;
import hr.algebra.festlog.service.EventService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/events")
public class EventMvcController {

    private final EventService eventService;

    public EventMvcController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) EventType type,
        @RequestParam(required = false) EventStatus status,
        @RequestParam(required = false) String city,
        Model model
    ) {
        boolean searching = query != null || type != null || status != null || city != null;
        model.addAttribute("events", searching
            ? eventService.search(query, type, status, city)
            : eventService.findAll());
        model.addAttribute("eventTypes", EventType.values());
        model.addAttribute("eventStatuses", EventStatus.values());
        model.addAttribute("query", query);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("city", city);
        return "events/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("event", eventService.findById(id));
            return "events/detail";
        } catch (NoSuchElementException e) {
            return "redirect:/events";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newForm(Model model) {
        model.addAttribute("event", new EventDto(
            null, "", "", "", "", "", "", null, null,
            null, null, null, "", "", null, null, null
        ));
        model.addAttribute("eventTypes", EventType.values());
        model.addAttribute("eventStatuses", EventStatus.values());
        model.addAttribute("editMode", false);
        return "events/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(
        @Valid @ModelAttribute("event") EventDto dto,
        BindingResult result,
        @AuthenticationPrincipal User currentUser,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("eventTypes", EventType.values());
            model.addAttribute("eventStatuses", EventStatus.values());
            model.addAttribute("editMode", false);
            return "events/form";
        }
        eventService.create(dto, currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "Event added to your log.");
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("event", eventService.findById(id));
            model.addAttribute("eventTypes", EventType.values());
            model.addAttribute("eventStatuses", EventStatus.values());
            model.addAttribute("editMode", true);
            return "events/form";
        } catch (NoSuchElementException e) {
            return "redirect:/events";
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("event") EventDto dto,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("eventTypes", EventType.values());
            model.addAttribute("eventStatuses", EventStatus.values());
            model.addAttribute("editMode", true);
            return "events/form";
        }
        eventService.update(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Event updated.");
        return "redirect:/events";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        eventService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Event removed.");
        return "redirect:/events";
    }
}
