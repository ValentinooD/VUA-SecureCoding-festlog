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
    private static final String ATTR_EVENT = "event";
    private static final String ATTR_EVENT_TYPES = "eventTypes";
    private static final String ATTR_EVENT_STATUSES = "eventStatuses";
    private static final String ATTR_EDIT_MODE = "editMode";
    private static final String ATTR_SUCCESS_MESSAGE = "successMessage";

    private static final String REDIRECT_EVENTS = "redirect:/events";

    private static final String TEMPLATE_FORM = "events/form";

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
        model.addAttribute(ATTR_EVENT_TYPES, EventType.values());
        model.addAttribute(ATTR_EVENT_STATUSES, EventStatus.values());
        model.addAttribute("query", query);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("city", city);
        return "events/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            model.addAttribute(ATTR_EVENT, eventService.findById(id));
            return "events/detail";
        } catch (NoSuchElementException _) {
            return REDIRECT_EVENTS;
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newForm(Model model) {
        model.addAttribute(ATTR_EVENT, new EventDto(
            null, "", "", "", "", "", "", null, null,
            null, null, null, "", "", null, null, null
        ));
        model.addAttribute(ATTR_EVENT_TYPES, EventType.values());
        model.addAttribute(ATTR_EVENT_STATUSES, EventStatus.values());
        model.addAttribute(ATTR_EDIT_MODE, false);
        return TEMPLATE_FORM;
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
            model.addAttribute(ATTR_EVENT_TYPES, EventType.values());
            model.addAttribute(ATTR_EVENT_STATUSES, EventStatus.values());
            model.addAttribute(ATTR_EDIT_MODE, false);
            return TEMPLATE_FORM;
        }
        eventService.create(dto, currentUser);
        redirectAttributes.addFlashAttribute(ATTR_SUCCESS_MESSAGE, "Event added to your log.");
        return REDIRECT_EVENTS;
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            model.addAttribute(ATTR_EVENT, eventService.findById(id));
            model.addAttribute(ATTR_EVENT_TYPES, EventType.values());
            model.addAttribute(ATTR_EVENT_STATUSES, EventStatus.values());
            model.addAttribute(ATTR_EDIT_MODE, true);
            return TEMPLATE_FORM;
        } catch (NoSuchElementException _) {
            return REDIRECT_EVENTS;
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
            model.addAttribute(ATTR_EVENT_TYPES, EventType.values());
            model.addAttribute(ATTR_EVENT_STATUSES, EventStatus.values());
            model.addAttribute(ATTR_EDIT_MODE, true);
            return TEMPLATE_FORM;
        }
        eventService.update(id, dto);
        redirectAttributes.addFlashAttribute(ATTR_SUCCESS_MESSAGE, "Event updated.");
        return REDIRECT_EVENTS;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        eventService.delete(id);
        redirectAttributes.addFlashAttribute(ATTR_SUCCESS_MESSAGE, "Event removed.");
        return REDIRECT_EVENTS;
    }
}
