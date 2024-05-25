package com.nomad.socialspring.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public Page<NotificationResponse> getNotifications(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return notificationService.getNotifications(page, size);
  }
}
